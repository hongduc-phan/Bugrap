package components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;

@Tag("progress-bar")
public class ProgressBar extends Component implements HasComponents {
    public ProgressBar(int total, int progressBarWidth, int firstQuantity, int secondQuantity, int thirdQuantity) {
        Div wrapper = new Div();
        wrapper.getStyle().set("display", "flex").set("width", "100%");

        Div firstPart = new Div();
        firstPart.setText(String.valueOf(firstQuantity));
        firstPart.getStyle().set("background-color", "#4e4e58")
                .set("padding", "0 4px 0 8px")
                .set("border-top-left-radius", "20px")
                .set("border-bottom-left-radius", "20px")
                .set("min-width", "28px").set("width", firstQuantity * progressBarWidth / total + "px");

        Div secondPart = new Div();
        secondPart.setText(String.valueOf(secondQuantity));
        secondPart.getStyle().set("background-color", "#8e8e96")
                .set("padding", "0 4px")
                .set("min-width", "28px").set("width", secondQuantity * progressBarWidth / total + "px");

        Div thirdPart = new Div();
        thirdPart.setText(String.valueOf(thirdQuantity));
        thirdPart.getStyle().set("background-color", "#cbcbd2")
                .set("padding", "0 4px 0 8px")
                .set("border-top-right-radius", "20px")
                .set("border-bottom-right-radius", "20px")
                .set("min-width", "28px").set("width", thirdQuantity * progressBarWidth / total + "px");;

        wrapper.add(firstPart);
        wrapper.add(secondPart);
        wrapper.add(thirdPart);

        add(wrapper);
    }
}
