package GameExceptions;

public class STAGException extends Exception
{
    String errorMessage;

    public STAGException()
    {
        errorMessage = "No specific cause of error stated. Check inputs?";
    }

    public STAGException(String errorMessage) { this.errorMessage = errorMessage; }

    @Override
    public String toString() { return errorMessage; }
}
