import exception.TourServiceValidationException;
import model.Client;


public class Main {
    public static void main(String[] args) {
        var i = Integer.MAX_VALUE + 10;
        System.out.println("Hello " + i);

        try {
            var client = new Client("", "", "", "", 0);
        }catch (TourServiceValidationException e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("finally");
        }
    }
}
