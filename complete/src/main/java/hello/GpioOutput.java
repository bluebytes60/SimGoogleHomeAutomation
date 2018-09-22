package hello;

/**
 * Created by bluebyte60 on 9/21/18.
 */

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

public class GpioOutput {

    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void go() throws InterruptedException, PlatformAlreadyAssignedException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Output Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin as an output pin and turn on
        final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "My Output", PinState.HIGH);

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

        // wait
        Thread.sleep(500);

        // --------------------------------------------------------------------------

        // tset gpio pin state to LOW
        console.emptyLine();
        console.println("Setting output pin state is set to LOW.");
        output.low(); // or ... output.setState(PinState.LOW);


        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        gpio.unprovisionPin(output);
    }
}