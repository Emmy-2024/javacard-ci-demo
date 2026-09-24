
package fr.insa;

import javacard.framework.*;

public class HelloWorldApplet extends Applet {

    // ⚠️ Volontairement vulnérable pour la démonstration CodeQL :
    // PIN codé en dur dans l'application.
    private static final byte[] DEMO_PIN = {
        (byte) '1',
        (byte) '2',
        (byte) '3',
        (byte) '4'
    };

    private OwnerPIN pin;

    private static final byte[] BONJOUR = {
        (byte) 'B',
        (byte) 'o',
        (byte) 'n',
        (byte) 'j',
        (byte) 'o',
        (byte) 'u',
        (byte) 'r'
    };

    private static final byte BJ_INS = (byte) 0x02;

    private static final byte[] HELLO_WORLD = {
        (byte) 'H',
        (byte) 'e',
        (byte) 'l',
        (byte) 'l',
        (byte) 'o',
        (byte) ' ',
        (byte) 'W',
        (byte) 'o',
        (byte) 'r',
        (byte) 'l',
        (byte) 'd'
    };

    private static final short HELLO_WORLD_LEN = 11;

    private static final byte CLA_APPLET = (byte) 0x80;
    private static final byte INS_GET_HELLO = (byte) 0x01;

    private HelloWorldApplet() {
        pin = new OwnerPIN((byte) 3, (byte) 4);
        initPin();
        register();
    }

    private void initPin() {
        // ⚠️ PIN codé en dur volontairement pour la démonstration CodeQL.
        pin.update(DEMO_PIN, (short) 0, (byte) 4);
    }

    public static void install(
        byte[] bArray,
        short bOffset,
        byte bLength
    ) {
        new HelloWorldApplet();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();

        byte cla = (byte) (
            buffer[ISO7816.OFFSET_CLA] & 0xFF
        );

        byte ins = (byte) (
            buffer[ISO7816.OFFSET_INS] & 0xFF
        );

        if (cla != CLA_APPLET) {
            ISOException.throwIt(
                ISO7816.SW_CLA_NOT_SUPPORTED
            );
        }

        switch (ins) {

            case INS_GET_HELLO:
                sendHelloWorld(apdu);
                break;

            case BJ_INS:
                getBonjour(apdu);
                break;

            default:
                ISOException.throwIt(
                    ISO7816.SW_INS_NOT_SUPPORTED
                );
        }
    }

    private void sendHelloWorld(APDU apdu) {
        byte[] buffer = apdu.getBuffer();

        Util.arrayCopyNonAtomic(
            HELLO_WORLD,
            (short) 0,
            buffer,
            (short) 0,
            HELLO_WORLD_LEN
        );

        apdu.setOutgoingAndSend(
            (short) 0,
            HELLO_WORLD_LEN
        );
    }

    private void getBonjour(APDU apdu) {
        byte[] buffer = apdu.getBuffer();

        short length = (short) BONJOUR.length;

        Util.arrayCopyNonAtomic(
            BONJOUR,
            (short) 0,
            buffer,
            (short) 0,
            length
        );

        apdu.setOutgoingAndSend(
            (short) 0,
            length
        );
    }
}


