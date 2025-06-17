package quickchat;

public class Login {
    private String username;
    private String password;
    private String cell;
    private String firstName;
    private String lastName;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{8,}$");
    }

    public boolean checkCellPhoneNumber(String phone) {
        return phone.matches("^\\+\\d{1,3}\\d{7,9}$");
    }

    public String registerUser(String username, String password, String cell, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        StringBuilder msg = new StringBuilder();

        if (checkUserName(username)) {
            this.username = username;
            msg.append("Username successfully captured.\n");
        } else {
            msg.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
        }

        if (checkPasswordComplexity(password)) {
            this.password = password;
            msg.append("Password successfully captured.\n");
        } else {
            msg.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
        }

        if (checkCellPhoneNumber(cell)) {
            this.cell = cell;
            msg.append("Cell phone number successfully captured.\n");
        } else {
            msg.append("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.\n");
        }

        return msg.toString();
    }

    public boolean loginUser(String inputUsername, String inputPassword) {
        return inputUsername.equals(username) && inputPassword.equals(password);
    }

    public String returnLoginStatus(boolean success) {
        return success
            ? "Welcome " + firstName + ", " + lastName + " it is great to see you again."
            : "Username or password incorrect, please try again.";
    }
}
