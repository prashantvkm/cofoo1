package cofoo.exceptions;

public class CodeInvalidOrExpired extends RuntimeException{

    public CodeInvalidOrExpired(String exceptionStr){
        super(exceptionStr);
    }
}
