package cofoo.exceptions;

public class DuplicateAccountException extends RuntimeException  {

    public DuplicateAccountException(String exceptonStr){
        super(exceptonStr);
    }
}
