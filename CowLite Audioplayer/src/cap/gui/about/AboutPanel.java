/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.about;

import cap.gui.shared.SexyScrollPane;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
class AboutPanel extends SexyScrollPane {
    
    public AboutPanel(ColorScheme layout) {
        super(layout.general().getContentColor());
        
        JTextArea textArea = new JTextArea();
        textArea.setBackground(layout.general().getFrameColor());
        textArea.setForeground(layout.general().getContentColor());
        textArea.setEditable(false);
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(textArea.getFont().deriveFont(Font.BOLD, 18));
        
        textArea.setText("What the fuck did you just fucking say about me, you little bitch? I’ll have you know I graduated top of my class in the Navy Seals, and I’ve been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills.\n" +
"\n" +
"I am trained in gorilla warfare and I’m the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words.\n" +
"\n" +
"You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You’re fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that’s just with my bare hands.\n" +
"\n" +
"Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little “clever” comment was about to bring down upon you, maybe you would have held your fucking tongue.\n" +
"\n" +
"But you couldn’t, you didn’t, and now you’re paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it.\n" +
"\n" +
"You’re fucking dead, kiddo.");
        
        super.setViewport(super.createViewport());
        super.getViewport().add(textArea);
        super.getViewport().setBackground(layout.general().getContentColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder());
        super.setPreferredSize(super.getMinimumSize());
    }
    
}
