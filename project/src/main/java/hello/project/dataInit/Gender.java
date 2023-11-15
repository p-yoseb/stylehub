package hello.project.dataInit;

public enum Gender {

    male("남성"), female("여성");
    private final String description;

    Gender(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
