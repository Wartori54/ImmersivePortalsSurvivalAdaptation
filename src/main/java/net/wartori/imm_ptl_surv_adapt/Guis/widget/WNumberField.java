package net.wartori.imm_ptl_surv_adapt.Guis.widget;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.wartori.imm_ptl_surv_adapt.Global;

import java.util.function.Consumer;
import java.util.function.BiPredicate;

public class WNumberField extends WTextField {
    private BiPredicate<Integer, Integer> onNumberTyped;

    @Override
    public void onCharTyped(char ch) {
        try {
            int i = Integer.parseInt(String.valueOf(ch));
            String before = this.getText().substring(0, getCursor());
            String after = this.getText().substring(getCursor());
            String text = before+ch+after;

            if (onNumberTyped.test(i, Integer.parseInt(text))) {
                super.onCharTyped(ch);
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public void setOnNumberTyped(BiPredicate<Integer, Integer> onNumberTyped) {
        this.onNumberTyped = onNumberTyped;
    }

    public int getValue(){
        return Integer.parseInt(getText());
    }

}
