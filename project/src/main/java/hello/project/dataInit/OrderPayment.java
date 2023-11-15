package hello.project.dataInit;

public enum OrderPayment {

    credit("신용카드"), handPhone("휴대폰결제"), accountTransfer("계좌이체"), makeDeposit("무통장입금"), deposit("예치금"),;
    private final String description;

    OrderPayment(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
