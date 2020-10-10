import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class Main extends JFrame {
    // *** Initialize constants
    // Window size
    private static final int WIDTH = 800;

    private static final int HEIGHT = 600;

    // Button names
    // Define string constants here
    private static final String lineText = "Line";

    private static final String wipeText = "Wipe";

    private static final String rectText = "Rectangle";

    // Color handling
    // Use a vector to hold the names of the options
    private static final Vector<String> colors = new Vector<String>();

    // Declare string constants for the colors
    private static final String red = "Red";

    private static final String green = "Green";

    private static final String blue = "Blue";

    private static final String black = "Black";

    // *** Declares atomic elements
    // Declare bottons
    JButton lineButton;

    JButton wipeButton;

    JButton rectButton;

    // Declaration for colors combo box
    JComboBox colorsBox;

    // Pane declaration. No need to use more panels or canvas.
    protected JPanel toolPanel = new JPanel();

    protected JPanel colorsPanel = new JPanel();

    protected Canvas canvas = new Canvas();

    // *** Initialization of atomic elements
    public void initAtoms() {
        // Initilize the buttons
        lineButton = new JButton(lineText);
        wipeButton = new JButton(wipeText);
        rectButton = new JButton(rectText);
        // Add the names of the color options here using Vector's add method
        colors.add(black);
        colors.add(red);
        colors.add(green);
        colors.add(blue);
        // Initilizes the values of the colors you just added
        colorsBox = new JComboBox(colors);
        // To set a default selection use
        colorsBox.setSelectedIndex(0);// sets the default to be the first entry

        // Hint: do not forget to set the pen color before drawing
        // Wraps the color box with a panel for better appearance in the tool panel
        // Do not change
        colorsPanel.add(colorsBox);
    }// initAtoms


    // Layout components declaration
    Container contentPane;

    /**
     * Initializes layout . No need to change
     */
    public void initLayout() {
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
    }

    /**
     * Initializes the content pane
     */
    public void initContentPane() {
        // Add buttons to tool panel
        // Note: order of addition determines the order of appearance
        toolPanel.add(lineButton);
        toolPanel.add(wipeButton);
        toolPanel.add(colorsPanel);
        toolPanel.add(rectButton);
        // Adds the tool and canvas panels to the content pane
        // Note: No need to change the following two lines
        contentPane.add(toolPanel, BorderLayout.WEST);
        contentPane.add(canvas, BorderLayout.CENTER);
    }// initContentPane


    /**
     * Initializes the listeners for the buttons and the combo box
     */
    public void initListeners() {
        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.selectedFigure(Canvas.FigureTypes.LINE);
            }
        });
        wipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.wipe();
            }
        });
        colorsBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == colorsBox) {
                    String colorString = ((String) (colorsBox.getSelectedItem()));
                    // colorString holds the String value of the selected item in the
                    // colors box
                    canvas.setColor(colorString);
                }
            }
        });
        rectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.selectedFigure(Canvas.FigureTypes.RECT);
            }
        });
    }// of initListeners


    // Initializes entire containment hierarchy
    public void init() {
        initAtoms();
        initLayout();
        initContentPane();
        initListeners();
    }

    /* Constructor. No need to modify */
    // Main constructor
    public Main(String appTitle) {
        super(appTitle);
        init();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        validate();
    }

    /**
     * main method
     */
    public static void main(String[] args) {
        new Main("Draw Product Line");
    }
}