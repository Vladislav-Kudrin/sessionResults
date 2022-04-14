package sample.entities;

public class Student {
    private int marksheet;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String group;
    private byte gender;
    private String stringGender;
    private byte maritalStatus;
    private String stringMaritalStatus;


    public Student(int marksheet, String lastName, String firstName, String patronymic, String group, byte gender,
                   byte maritalStatus) {
        this.marksheet = marksheet;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.group = group;
        this.gender = gender;
        this.stringGender = (gender == 0) ? "Мужской" : "Женский";
        this.maritalStatus = maritalStatus;
        this.stringMaritalStatus = (maritalStatus == 0) ? "Нет" : (gender == 0) ? "Женат" : "Замужем";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setMaritalStatus(byte maritalStatus) {
        this.maritalStatus = maritalStatus;
        this.stringMaritalStatus = (maritalStatus == 0) ? "Нет" : (gender == 0) ? "Женат" : "Замужем";
    }

    public int getMarksheet() {
        return marksheet;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getGroup() {
        return group;
    }

    public byte getGender() {
        return gender;
    }

    public String getStringGender() {
        return stringGender;
    }

    public byte getMaritalStatus() {
        return maritalStatus;
    }

    public String getStringMaritalStatus() {
        return stringMaritalStatus;
    }
}
