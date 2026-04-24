import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class CrimeRecord {
    int year;
    double rate;

    CrimeRecord(int y, double r) {
        year = y;
        rate = r;
    }
}

class CityInfo {
    String name, population, area, safety, description;

    CityInfo(String n, String p, String a, String s, String d) {
        name = n;
        population = p;
        area = a;
        safety = s;
        description = d;
    }
}

public class CityMapDashboard extends JFrame {

    JTextField search;
    JLabel mapLabel;
    JTextArea info;
    ChartPanel chartPanel;

    Map<String, String> cityImages = new HashMap<>();
    Map<String, java.util.List<CrimeRecord>> data = new HashMap<>();
    Map<String, CityInfo> cityDetails = new HashMap<>();

    double zoom = 1.0;
    Image originalImage;

    public CityMapDashboard() {

        setTitle("Crime Analytics Dashboard");
        setSize(1200, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color bg = new Color(25,25,25);
        Color panel = new Color(35,35,35);

        getContentPane().setBackground(bg);

        // ===== TOP =====
        JPanel top = new JPanel();
        top.setBackground(panel);

        search = new JTextField(20);
        JButton btn = new JButton("Search");

        btn.setBackground(new Color(0,120,215));
        btn.setForeground(Color.WHITE);

        top.add(new JLabel("Enter City: "));
        top.add(search);
        top.add(btn);

        add(top, BorderLayout.NORTH);

        // ===== MAP =====
        mapLabel = new JLabel("", JLabel.CENTER);
        JScrollPane mapScroll = new JScrollPane(mapLabel);
        mapScroll.setPreferredSize(new Dimension(600,600));

        // ===== INFO =====
        info = new JTextArea();
        info.setEditable(false);
        info.setBackground(panel);
        info.setForeground(Color.GREEN);
        info.setFont(new Font("Consolas", Font.PLAIN, 14));
        info.setBorder(BorderFactory.createTitledBorder("City Details"));

        JScrollPane infoScroll = new JScrollPane(info);

        // ===== CHART =====
        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(400,220));

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(bg);
        right.add(infoScroll, BorderLayout.CENTER);
        right.add(chartPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapScroll, right);
        split.setDividerLocation(600);

        add(split, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel();
        bottom.setBackground(panel);

        JButton zoomIn = new JButton("Zoom In");
        JButton zoomOut = new JButton("Zoom Out");

        bottom.add(zoomIn);
        bottom.add(zoomOut);

        add(bottom, BorderLayout.SOUTH);

        loadData();

        btn.addActionListener(e -> searchCity());

        zoomIn.addActionListener(e -> {
            zoom *= 1.2;
            updateImage();
        });

        zoomOut.addActionListener(e -> {
            zoom /= 1.2;
            updateImage();
        });

        setVisible(true);
    }

    void loadData() {

        cityImages.put("delhi", "C:\\Users\\rd403\\Downloads\\delhi.png");
        cityImages.put("mumbai", "C:\\Users\\rd403\\Downloads\\mumbai.png");
        cityImages.put("kolkata", "C:\\Users\\rd403\\Downloads\\kolkata.png");
        cityImages.put("chandigarh", "C:\\Users\\rd403\\Downloads\\chd.png");

        data.put("delhi", Arrays.asList(
                new CrimeRecord(2018,20),
                new CrimeRecord(2019,22),
                new CrimeRecord(2020,26),
                new CrimeRecord(2021,30),
                new CrimeRecord(2022,32)
        ));

        data.put("mumbai", Arrays.asList(
                new CrimeRecord(2018,28),
                new CrimeRecord(2019,30),
                new CrimeRecord(2020,33),
                new CrimeRecord(2021,35),
                new CrimeRecord(2022,37)
        ));

        data.put("kolkata", Arrays.asList(
                new CrimeRecord(2018,18),
                new CrimeRecord(2019,20),
                new CrimeRecord(2020,22),
                new CrimeRecord(2021,24),
                new CrimeRecord(2022,26)
        ));

        data.put("chandigarh", Arrays.asList(
                new CrimeRecord(2018,10),
                new CrimeRecord(2019,11),
                new CrimeRecord(2020,13),
                new CrimeRecord(2021,15),
                new CrimeRecord(2022,16)
        ));

        cityDetails.put("delhi", new CityInfo("Delhi","3.2 Cr","1484 km²","High",
                "Capital of India. High crime due to dense population."));

        cityDetails.put("mumbai", new CityInfo("Mumbai","2.1 Cr","603 km²","Moderate",
                "Financial capital with organized crime cases."));

        cityDetails.put("kolkata", new CityInfo("Kolkata","1.5 Cr","206 km²","Moderate",
                "Cultural city with relatively lower crime."));

        cityDetails.put("chandigarh", new CityInfo("Chandigarh","12 Lakh","114 km²","Low",
                "Planned city with better safety and law enforcement."));
    }

    void searchCity() {
        String city = search.getText().toLowerCase();

        if (!cityImages.containsKey(city)) {
            info.setText("City not found!");
            return;
        }

        originalImage = new ImageIcon(cityImages.get(city)).getImage();
        zoom = 1.0;
        updateImage();
        showInfo(city);
    }

    void updateImage() {
        if (originalImage == null) return;

        int w = (int)(originalImage.getWidth(null)*zoom);
        int h = (int)(originalImage.getHeight(null)*zoom);

        Image scaled = originalImage.getScaledInstance(w,h,Image.SCALE_SMOOTH);
        mapLabel.setIcon(new ImageIcon(scaled));
    }

    void showInfo(String city) {
        CityInfo c = cityDetails.get(city);
        java.util.List<CrimeRecord> list = data.get(city);

        StringBuilder sb = new StringBuilder();

        sb.append("City: ").append(c.name).append("\n");
        sb.append("Population: ").append(c.population).append("\n");
        sb.append("Area: ").append(c.area).append("\n");
        sb.append("Safety: ").append(c.safety).append("\n\n");
        sb.append(c.description);

        info.setText(sb.toString());
        chartPanel.setData(list);
    }

    public static void main(String[] args) {
        new CityMapDashboard();
    }
}

// ===== FIXED GRAPH =====

class ChartPanel extends JPanel {

    java.util.List<CrimeRecord> data;

    ChartPanel() {
        setBackground(new Color(35,35,35)); // DARK BACKGROUND
    }

    void setData(java.util.List<CrimeRecord> d) {
        data = d;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        int barWidth = w / (data.size() * 2);
        int x = 40;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        g2.drawString("Crime Trend", w/2 - 50, 20);

        for (CrimeRecord r : data) {

            int barHeight = (int)(r.rate * 2.5);

            g2.setColor(new Color(0,150,255));
            g2.fillRoundRect(x, h - barHeight - 40, barWidth, barHeight, 10, 10);

            g2.setColor(Color.WHITE);
            g2.drawString("" + r.year, x, h - 15);
            g2.drawString(String.format("%.1f", r.rate), x, h - barHeight - 45);

            x += barWidth * 2;
        }
    }
}