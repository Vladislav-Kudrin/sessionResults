package sample.entities;

public class Grade {
    private int id;
    private int code;
    private String subject;
    private byte mark;
    private boolean isExam;

    public Grade(int id, int code, String subject, byte mark, boolean isExam) {
        this.id = id;
        this.code = code;
        this.subject = subject;
        this.mark = mark;
        this.isExam = isExam;
    }

    public Grade(int code, byte mark, boolean isExam) {
        this.code = code;
        this.mark = mark;
        this.isExam = isExam;
    }

    public int getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public String getSubject() {
        return subject;
    }

    public byte getMark() {
        return mark;
    }

    public boolean isExam() {
        return isExam;
    }
}
