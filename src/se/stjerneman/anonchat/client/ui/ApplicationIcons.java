package se.stjerneman.anonchat.client.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class ApplicationIcons {

    public static List<Image> getIcons () {
        List<Image> imageList = new ArrayList<>();

        Toolkit tk = Toolkit.getDefaultToolkit();

        imageList
                .add(tk.getImage(ApplicationIcons.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon128.png")));
        imageList
                .add(tk.getImage(ApplicationIcons.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon64.png")));
        imageList
                .add(tk.getImage(ApplicationIcons.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon32.png")));
        imageList
                .add(tk.getImage(ApplicationIcons.class
                        .getResource("/se/stjerneman/anonchat/client/ui/icons/chatIcon16.png")));

        return imageList;
    }
}
