package telran.employees.controller;

import java.util.ArrayList;

import telran.employees.services.EmployeesMethods;
import telran.employees.services.EmployeesMethodsMapsImpl;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class EmployeeAppl {
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		EmployeesMethods employees = new EmployeesMethodsMapsImpl();
		ArrayList<Item> items = EmployeeActions.getEmployeesMenuItems(employees);
	
		Menu menu = new Menu("Employees Menu", items);
	
		menu.perform(io);
	
	}
}
