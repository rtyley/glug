package glug.gui;

import javax.swing.*;

import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.KeyEvent.*;
import static java.util.Arrays.asList;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static javax.swing.KeyStroke.getKeyStroke;

public class KeystrokeSupplementor {
    public void hackCtrlPlus(JComponent component) {
        KeyStroke plusKeyOnNonEnglishKeyboards = getKeyStroke(VK_PLUS, CTRL_MASK);
        KeyStroke plusKeyOnEnglishKeyboards = getKeyStroke(VK_EQUALS, CTRL_MASK);
        KeyStroke plusKeyOnTheNumpad = getKeyStroke(VK_ADD, CTRL_MASK);

        supplementKeystokeMappingOn(component, plusKeyOnNonEnglishKeyboards, asList(plusKeyOnEnglishKeyboards, plusKeyOnTheNumpad));
    }

    public void hackCtrlMinus(JComponent component) {
        KeyStroke minusKey = getKeyStroke(VK_EQUALS, CTRL_MASK);
        KeyStroke minusKeyOnTheNumpad = getKeyStroke(VK_ADD, CTRL_MASK);

        supplementKeystokeMappingOn(component, minusKey, asList(minusKeyOnTheNumpad));
    }

    private void supplementKeystokeMappingOn(JComponent component, KeyStroke keystrokeToSupplement, Iterable<KeyStroke> supplementalKeystrokes) {
        InputMap inputMap = SwingUtilities.getUIInputMap(component, WHEN_IN_FOCUSED_WINDOW);

        Object actionMapKey = inputMap.get(keystrokeToSupplement);

        for (KeyStroke supplementalKeystroke : supplementalKeystrokes) {
            inputMap.put(supplementalKeystroke, actionMapKey);
        }
    }
}
