package opgave01.application.model;

public class Employee {
    private String name;
    private int wage;// hourly wage
    private int employmentYear;

    // link to company class (--> 0..1)
    private Company company;

    /**
     *
     * @param name name of the employee
     * @param wage hourly wage of the employee
     */
    public Employee(String name, int wage) {
        this.name = name;
        this.wage = wage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public int getEmploymentYear() {
        return employmentYear;
    }

    public void setEmploymentYear(int employmentYear) {
        this.employmentYear = employmentYear;
    }

    @Override
    public String toString() {
        return name + " (kr " + wage + ")";
    }

    public Company getCompany() {
        return company;
    }

    /**
     * Sets the company as this employees company, if they aren't connected
     *
     * @param company the new company of the employee
     */
    public void setCompany(Company company) {
        if (this.company != company) {
            Company oldCompany = this.company;
            if (oldCompany != null) {
                oldCompany.removeEmployee(this);
            }
            this.company = company;
            if (company != null)
                company.addEmployee(this);
        }
    }

    /**
     * Returns the weekly salary of this employee.
     */
    public int weeklySalary() {
        return wage * company.getHours();
    }
}