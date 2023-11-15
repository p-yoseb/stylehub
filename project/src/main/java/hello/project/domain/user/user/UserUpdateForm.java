package hello.project.domain.user.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdateForm {
//    su-dong
    private Long id;
    @NotBlank
    private String loginId;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotNull
    private Integer year;
    @NotNull
    private Integer month;
    @NotNull
    private Integer day;
    @NotBlank
    private String gender;
    @NotBlank
    private String postal;
    @NotBlank
    private String address;
    @NotBlank
    private String email;
    @NotNull
    private Long phone;
    private Boolean advertising;
    @NotNull
    private double deposit;
    @NotNull
    private Long categoryId;

//    auto
    private double amount;
    private double coupon;
    private String grade;
    private Double ratio;
    private Boolean admin;

    public UserUpdateForm() {
    }

    public UserUpdateForm(String loginId, String name, String password, Integer year, Integer month, Integer day, String gender, String postal, String address, String email, Long phone, Boolean advertising, double deposit, Long categoryId) {

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

    public UserUpdateForm(String loginId, String name, String password, Integer year, Integer month, Integer day, String gender, String postal, String address, String email, Long phone, Boolean advertising, double deposit, Long categoryId, double amount, double coupon, String grade, Double ratio, Boolean admin) {
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
