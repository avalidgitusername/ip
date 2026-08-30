package recordbase.exceptions;

public class RecordException extends RuntimeException {

    public RecordException(String errorMessage) {
        super(errorMessage);
    }

    public RecordException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
