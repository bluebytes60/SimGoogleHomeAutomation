package hello;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

/**
 * Created by bluebyte60 on 9/21/18.
 */
public class GPIOSingleton {
    private static GPIOSingleton instance = new GPIOSingleton();

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    final Console console = new Console();

    final GpioController gpio = GpioFactory.getInstance();

    // provision gpio pin as an output pin and turn on
    final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "My Output", PinState.LOW);

    private GPIOSingleton() {
        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Output Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();
        // create gpio controller


        // set shutdown state for this pin: keep as output pin, set to low state
        output.setShutdownOptions(false, PinState.LOW);

        // create a pin listener to print out changes to the output gpio pin state
        output.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                console.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " +
                        ConsoleColor.conditional(
                                event.getState().isHigh(), // conditional expression
                                ConsoleColor.GREEN,        // positive conditional color
                                ConsoleColor.RED,          // negative conditional color
                                event.getState()));        // text to display
            }
        });

        // prompt user that we are ready
        console.println(" ... Successfully provisioned output pin: " + output.toString());
        console.emptyLine();
        console.box("The GPIO output pin states will cycle HIGH and LOW states now.");
        console.emptyLine();

        // notify user of current pin state
        console.println("--> [" + output.toString() + "] state was provisioned with state = " +
                ConsoleColor.conditional(
                        output.getState().isHigh(), // conditional expression
                        ConsoleColor.GREEN,         // positive conditional color
                        ConsoleColor.RED,           // negative conditional color
                        output.getState()));        // text to display

    }

    public static GPIOSingleton getInstance() {
        return instance;
    }

    public synchronized void onAWhile() throws InterruptedException {
        // tset gpio pin state to LOW
        console.emptyLine();
        console.println("Setting output pin state is set to LOW.");
        output.high(); // or ... output.setState(PinState.LOW);
        Thread.sleep(1000);
        output.low();
        Thread.sleep(1000);
    }
}
