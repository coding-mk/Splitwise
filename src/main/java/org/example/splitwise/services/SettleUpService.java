package org.example.splitwise.services;

import org.example.splitwise.models.*;
import org.example.splitwise.models.Expense;
import org.example.splitwise.repositories.ExpenseRepository;
import org.example.splitwise.repositories.ExpenseUserRepository;
import org.example.splitwise.repositories.GroupRepository;
import org.example.splitwise.repositories.UserRepository;
import org.example.splitwise.strategy.SettleUpStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SettleUpService {

    private UserRepository userRepository;
    private ExpenseUserRepository expenseUserRepository;
    private SettleUpStrategy settleUpStrategy;
    private GroupRepository groupRepository;
    private ExpenseRepository expenseRepository;

    public SettleUpService(UserRepository userRepository,
                           ExpenseUserRepository expenseUserRepository,
                           SettleUpStrategy settleUpStrategy,
                           GroupRepository groupRepository,
                           ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseUserRepository = expenseUserRepository;
        this.settleUpStrategy = settleUpStrategy;
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> settleUpUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            throw new RuntimeException("Invalid userId - " + userId);
        }

        User user = optionalUser.get();

        List<ExpenseUser> expenseUsers = expenseUserRepository.findAllByUser(user);

        Set<Expense> expenses = new HashSet<>();

        for(ExpenseUser expenseUser : expenseUsers){
            expenses.add(expenseUser.getExpense());
        }

        //Settle up the expense
        List<Expense> settleUpExpense = settleUpStrategy.settleUp(expenses.stream().toList());

        List<Expense> expensesToReturn = new ArrayList<>();
        for(Expense expense : settleUpExpense){
            for(ExpenseUser expenseUser : expense.getExpenseUsers()){
                if(expenseUser.getUser().equals(user)){
                    expensesToReturn.add(expense);
                    break;
                }
            }
        }

        return expensesToReturn;
    }
}
