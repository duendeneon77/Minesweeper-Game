package minesweeperImportantPackages.vision;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import minesweeperImportantPackages.model.Field;
import minesweeperImportantPackages.model.FieldEvent;
import minesweeperImportantPackages.model.FieldObserver;



@SuppressWarnings("serial")
public class FieldButton extends JButton 
    implements FieldObserver, MouseListener {

    private final Color BG_DEFAULT = new Color(184, 184, 184);
    private final Color BG_MARKED = new Color(8, 179, 247);
    private final Color BG_EXPLODE = new Color(189, 66, 68);
    private final Color TEXT_GREEN = new Color(0, 100, 0);

    private Field field;

    public FieldButton(Field field) {
        this.field = field;
        setBackground(BG_DEFAULT);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        field.registerObserver(this);
    }

    @Override
    public void eventOccurred(Field field, FieldEvent event) {
        switch (event) {
        case OPEN:
            applyOpenStyle();
            break;
        case MARK:
            applyMarkStyle();
            break;
        case EXPLODE:
            applyExplodeStyle();
            break;
        default:
            applyDefaultStyle();
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void applyDefaultStyle() {
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void applyExplodeStyle() {
        setBackground(BG_EXPLODE);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void applyMarkStyle() {
        setBackground(BG_MARKED);
        setForeground(Color.BLACK);
        setText("M");
    }

    private void applyOpenStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (field.isMined()) {
            setBackground(BG_EXPLODE);
            return;
        }

        setBackground(BG_DEFAULT);

        switch (field.minesInNeighborhood()) {
        case 1:
            setForeground(TEXT_GREEN);
            break;
        case 2:
            setForeground(Color.BLUE);
            break;
        case 3:
            setForeground(Color.YELLOW);
            break;
        case 4:
        case 5:
        case 6:
            setForeground(Color.RED);
            break;
        default:
            setForeground(Color.PINK);
        }

        String value = !field.safeNeighborhood() ? 
                field.minesInNeighborhood() + "" : "";
        setText(value);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            field.open();
        } else {
            field.toggleMark();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
