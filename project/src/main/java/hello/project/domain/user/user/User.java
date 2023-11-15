package hello.project.domain.user.user;

import lombok.Data;

@Data
public class User {
//    su-dong
    private Long id;
    private String loginId;
    private String name;
    private String password;
    private Integer year;
    private Integer month;
    private Integer day;
    private String gender;
    private String postal;
    private String address;
    private String email;
    private Long phone;
    private Boolean advertising;
    private double deposit;
    private Long categoryId;
//    auto
    private double amount;
    private double coupon;
    private String grade;
    private Double ratio;
    private Boolean admin;

    public User() {
    }

    public User(String loginId, String name, String password, Integer year, Integer month, Integer day, String gender, String postal, String address, String email, Long phone, Boolean advertising, double deposit, Long categoryId) {

        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.year = year;
        this.month = month;
        this.day = day;
        this.gender = gender;
        this.postal = postal;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.advertising = advertising;
        this.deposit = deposit;
        this.categoryId = categoryId;
    }

    public User(String loginId, String name, String password, Integer year, Integer month, Integer day, String gender, String postal, String address, String email, Long phone, Boolean advertising, double deposit, Long categoryId, double amount, double coupon, String grade, Double ratio, Boolean admin) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.year = year;
        this.month = month;
        this.day = day;
        this.gender = gender;
        this.postal = postal;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.advertising = advertising;
        this.deposit = deposit;
        this.categoryId = categoryId;
        this.amount = amount;
        this.coupon = coupon;
        this.grade = grade;
        this.ratio = ratio;
        this.admin = admin;
    }
}
