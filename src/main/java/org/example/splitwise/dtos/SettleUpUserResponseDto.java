package org.example.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.splitwise.models.Expense;

import java.util.List;

@Getter
@Setter
public class SettleUpUserResponseDto {
    private List<Expense> expenses;
}
