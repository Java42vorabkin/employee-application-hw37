package telran.employees.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import telran.employees.dto.Employee;
import telran.employees.dto.ReturnCode;
import telran.employees.services.EmployeesMethods;
import telran.view.InputOutput;
import telran.view.Item;

public class EmployeeActions {
	
	private static final int MIN_SALARY = 3000;
	private static final int MAX_SALARY = 50000;
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 120;
	private static EmployeesMethods employees;
	
	private EmployeeActions(){
		
	}
	static public ArrayList<Item> getEmployeesMenuItems(EmployeesMethods employees){
		EmployeeActions.employees = employees;
		ArrayList<Item> items = new ArrayList<>();
		items.add(Item.of(" Add new Employee", EmployeeActions::addEmployee));
		items.add(Item.of(" Remove Employee", EmployeeActions::removeEmployee));
		items.add(Item.of(" Get Employee by ID", EmployeeActions::getEmployee));
		items.add(Item.of(" Get all Employees", EmployeeActions::getAllEmployees));
		items.add(Item.of(" Get Employees by age", EmployeeActions::getEmployeesByAge));
		items.add(Item.of(" Get Employees by salary", EmployeeActions::getEmployeesBySalary));
		items.add(Item.of(" Get Employees by department", EmployeeActions::getEmployeesByDepartment));
		items.add(Item.of(" Get Employees by depatment and salary", EmployeeActions::getEmployeesByDepartmentAndSalary));
		items.add(Item.of(" Update salary", EmployeeActions::updateSalary));
		items.add(Item.of("Update department", EmployeeActions::updateDepartment));
		items.add(Item.exit());

		return items;
	}
	static private Employee enterEmployee(InputOutput io) {
		long id = readEmployeeId(io);
		if(employees.getEmployee(id) != null) {
			throw new IllegalArgumentException("Jenya");
		}
		String name = io.readStringPredicate("Enter name", "Name may contain only letters with first capital",
				str -> str.matches("[A-Z][a-z]+"));
		LocalDate birthDate = io.readDate("Enter birthdate in the yyyy-MM-dd format");
		String salaryPrompt = String.format("Enter Salary, %d - %d", MIN_SALARY, MAX_SALARY);
		int salary = io.readInt(salaryPrompt, MIN_SALARY, MAX_SALARY);
		Set<String> departments = EmployeeActions.getListDepartments();
		String department = io.readStringOption("Enter department " + departments, departments);
		return  new Employee(id, name, birthDate, salary, department);
	}
	 private static Set<String> getListDepartments(){
		return new HashSet<>(Arrays.asList("QA", "Development", "Management"));
	}

	static private void addEmployee(InputOutput io) {
		Employee enteredEmployee = EmployeeActions.enterEmployee(io);
		io.writeObjectLine(employees.addEmployee(enteredEmployee));
	}
	static private  void removeEmployee(InputOutput io) {
		long employeeId = readEmployeeId(io);
		io.writeObjectLine(employees.removeEmployee(employeeId));
	}
	static private  void getAllEmployees(InputOutput io) {
		fromIterableToPrint(io, employees.getAllEmployees());
	}
	static private  void getEmployee(InputOutput io) {
		long employeeId = readEmployeeId(io);
		Employee empl = employees.getEmployee(employeeId);
		if(empl != null) {
			io.writeObjectLine(empl);
		} else {
			io.writeObjectLine("Employee isn't found. id="+employeeId);
		}
	}

	static private  void getEmployeesByAge(InputOutput io) {
		String minAgePrompt = String.format("Enter min age, %d - %d", MIN_AGE, MAX_AGE);
		int ageFrom = io.readInt(minAgePrompt, MIN_AGE, MAX_AGE);
		String maxAgePrompt = String.format("Enter max age, %d - %d", ageFrom, MAX_AGE);
		int ageTo = io.readInt(maxAgePrompt, ageFrom, MAX_AGE);	
		fromIterableToPrint(io, employees.getEmployeesByAge(ageFrom, ageTo));
	}

	static private  void getEmployeesBySalary(InputOutput io) {
		String minSalaryPrompt = String.format("Enter min Salary, %d - %d", MIN_SALARY, MAX_SALARY);
		int minSalary = io.readInt(minSalaryPrompt, MIN_SALARY, MAX_SALARY);
		String maxSalaryPrompt = String.format("Enter max Salary, %d - %d", minSalary, MAX_SALARY);
		int maxSalary = io.readInt(maxSalaryPrompt, minSalary, MAX_SALARY);	
		fromIterableToPrint(io, employees.getEmployeesBySalary(minSalary, maxSalary));
	}

	static private  void getEmployeesByDepartment(InputOutput io) {
		Set<String> departments = getListDepartments();
		String department = io.readStringOption("Enter department " + departments, departments);
		fromIterableToPrint(io, employees.getEmployeesByDepartment(department));
	}

	static private  void getEmployeesByDepartmentAndSalary(InputOutput io) {
		Set<String> departments = getListDepartments();
		String department = io.readStringOption("Enter department from list" + departments, departments);
	
		String minSalaryPrompt = String.format("Enter min Salary, %d - %d", MIN_SALARY, MAX_SALARY);
		int minSalary = io.readInt(minSalaryPrompt, MIN_SALARY, MAX_SALARY);
		String maxSalaryPrompt = String.format("Enter max Salary, %d - %d", minSalary, MAX_SALARY);
		int maxSalary = io.readInt(maxSalaryPrompt, minSalary, MAX_SALARY);
		fromIterableToPrint(io, employees.getEmployeesByDepartmentAndSalary(department, minSalary, maxSalary));
	}
	static private  void updateSalary(InputOutput io) {
		long emplId = readEmployeeId(io);
		int newSalary = io.readInt("Enter new Salary", MIN_SALARY, MAX_SALARY);
		io.writeObjectLine(employees.updateSalary(emplId, newSalary));
	}
	static private  void updateDepartment(InputOutput io) {
		long emplId = readEmployeeId(io);
		Set<String> departments = getListDepartments();
		String newDepartment = io.readStringOption("Enter department from list" + departments, departments);
		io.writeObjectLine(employees.updateDepartment(emplId, newDepartment));

	}
	static private void fromIterableToPrint(InputOutput io, Iterable<Employee> employeesIt) {
		Iterator<Employee> itr = employeesIt.iterator();
		boolean found = false;
		while(itr.hasNext()) {
			io.writeObjectLine(itr.next());
			found = true;
		}
		if(!found) {
			io.writeObjectLine("The list of employees is empty");
		}
	}
	static private long readEmployeeId(InputOutput io) {
		String strId = io.readStringPredicate("EnterID", "ID contains digits only, length=9",
				str -> str.matches("[0-9]{9}"));
		return Long.parseLong(strId);
	}

}
