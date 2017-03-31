public class GreetingsApp {

    public static void main(String[] args) {
        String name = "";
        if (args.length > 0) {
            name = " " + args[0];
        }
        System.out.println("Hi " + name + "!");
    }
}
