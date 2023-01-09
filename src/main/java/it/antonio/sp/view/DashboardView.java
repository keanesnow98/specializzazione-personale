package it.antonio.sp.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.QualificationService;
import it.antonio.sp.service.SpecialtyService;

@ManagedBean
@RequestScope
public class DashboardView {
	private BarChartModel specialtiesBarRag;
	private DonutChartModel specialtiesSaf;
	private BarChartModel specialtiesBar;
	private LineChartModel qualificationsLine;
	private PieChartModel turnoAPie;
	private PieChartModel turnoBPie;
	private PieChartModel turnoCPie;
	private PieChartModel turnoDPie;
	private PieChartModel turnoABCDG5DiscPie;
	
	private List<String> defaultBackgroundColors;

	private List<String> defaultBorderColors;
	
	@Autowired
	SpecialtyService specialtyService;
	
	@Autowired
	QualificationService qualificationService;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	
	public DonutChartModel getSpecialtiesSaf() {
		return specialtiesSaf;
	}

	public void setSpecialtiesSaf(DonutChartModel specialtiesSaf) {
		this.specialtiesSaf = specialtiesSaf;
	}

	public BarChartModel getSpecialtiesBarRag() {
		return specialtiesBarRag;
	}

	public void setSpecialtiesBarRag(BarChartModel specialtiesBarRag) {
		this.specialtiesBarRag = specialtiesBarRag;
	}
	
	public BarChartModel getSpecialtiesBar() {
		return specialtiesBar;
	}
	
	public void setSpecialtiesBar(BarChartModel specialtiesBar) {
		this.specialtiesBar = specialtiesBar;
	}
	
	public LineChartModel getQualificationsLine() {
		return qualificationsLine;
	}
	
	public void setQualificationsLine(LineChartModel qualificationsLine) {
		this.qualificationsLine = qualificationsLine;
	}
	
	public PieChartModel getTurnoAPie() {
		return turnoAPie;
	}
	
	public void setTurnoAPie(PieChartModel turnoAPie) {
		this.turnoAPie = turnoAPie;
	}
	
	public PieChartModel getTurnoBPie() {
		return turnoBPie;
	}
	
	public void setTurnoBPie(PieChartModel turnoBPie) {
		this.turnoBPie = turnoBPie;
	}
	
	public PieChartModel getTurnoCPie() {
		return turnoCPie;
	}
	
	public void setTurnoCPie(PieChartModel turnoCPie) {
		this.turnoCPie = turnoCPie;
	}
	
	public PieChartModel getTurnoDPie() {
		return turnoDPie;
	}
	
	public void setTurnoDPie(PieChartModel turnoDPie) {
		this.turnoDPie = turnoDPie;
	}
	
	public PieChartModel getTurnoABCDG5DiscPie() {
		return turnoABCDG5DiscPie;
	}

	public void setTurnoABCDG5DiscPie(PieChartModel turnoABCDG5DiscPie) {
		this.turnoABCDG5DiscPie = turnoABCDG5DiscPie;
	}
	
	@PostConstruct
	public void init() {
		defaultBackgroundColors = new ArrayList<String>();
		defaultBackgroundColors.add("rgba(255, 99, 132, 0.2)");
		defaultBackgroundColors.add("rgba(255, 159, 64, 0.2)");
		defaultBackgroundColors.add("rgba(255, 205, 86, 0.2)");
		defaultBackgroundColors.add("rgba(75, 192, 192, 0.2)");
		defaultBackgroundColors.add("rgba(54, 162, 235, 0.2)");
		defaultBackgroundColors.add("rgba(153, 102, 255, 0.2)");
		defaultBackgroundColors.add("rgba(201, 203, 207, 0.2)");
		defaultBackgroundColors.add("rgba(198, 3, 252, 0.2)");
		defaultBackgroundColors.add("rgba(94, 99, 132, 0.2)");
		defaultBackgroundColors.add("rgba(255, 10, 64, 0.2)");
		defaultBackgroundColors.add("rgba(155, 25, 86, 0.2)");
		defaultBackgroundColors.add("rgba(75, 132, 92, 0.2)");
		defaultBackgroundColors.add("rgba(154, 62, 235, 0.2)");
		defaultBackgroundColors.add("rgba(153, 12, 25, 0.2)");
		defaultBackgroundColors.add("rgba(100, 203, 100, 0.2)");
		defaultBackgroundColors.add("rgba(60, 30, 252, 0.2)");

		defaultBorderColors = new ArrayList<String>();
        defaultBorderColors.add("rgb(255, 99, 132)");
        defaultBorderColors.add("rgb(255, 159, 64)");
        defaultBorderColors.add("rgb(255, 205, 86)");
        defaultBorderColors.add("rgb(75, 192, 192)");
        defaultBorderColors.add("rgb(54, 162, 235)");
        defaultBorderColors.add("rgb(153, 102, 255)");
        defaultBorderColors.add("rgb(201, 203, 207)");
        defaultBorderColors.add("rgb(198, 3, 252)");
		
		createSpecialtiesBar();
		createQualificationsLine();
		createTurnoAPie();
		createTurnoBPie();
		createTurnoCPie();
		createTurnoDPie();
		createTurnoABCDG5DiscPie();
		createSpecialtiesBarRag();
		createSpecialtiesSaf();
	}
	
	public Integer getAnagraphicsTotal() {
		return anagraphicService.findAll().size();
	}
	
	public Integer getAnagraphicsWithSpecialtyValid() {
		return anagraphicService.findAllSpecialtyValid().size();
	}
	
	public Integer getAnagraphicsWithSpecialtyExpired() {
		return anagraphicService.findAllSpecialtyExpired().size();
	}
	
	public Integer getAnagraphicsWithSpecialtyEmpty() {
		return anagraphicService.findAllSpecialtyEmpty().size();
	}
	
	//SPECIALIZZAZIONI RAGGRUPPATE
	public void createSpecialtiesBarRag() {
		specialtiesBarRag = new BarChartModel();
        ChartData data = new ChartData();
        
        Map<String,Number> specialtyCountsByName = anagraphicService.getGroupedSpecialtyCounts();
        
        List<BarChartDataSet> dataSets = new ArrayList<>();
        
        Random rand = new Random();
        for(Map.Entry<String, Number> entry : specialtyCountsByName.entrySet()) {
        	if (entry.getValue().intValue() == 0) {
        		continue;
        	}
        	
        	BarChartDataSet dataset = new BarChartDataSet();
        	dataset.setLabel(entry.getKey());
        	dataset.setBackgroundColor(defaultBackgroundColors.get(rand.nextInt(defaultBackgroundColors.size())));
        	dataset.setBorderColor(defaultBackgroundColors.get(rand.nextInt(defaultBackgroundColors.size())));
        	//dataset.setBorderColor(defaultBorderColors.get(rand.nextInt()));
        	dataset.setBorderWidth(1);
            List<Number> _values = new ArrayList<>();
            _values.add(entry.getValue());
            dataset.setData(_values);
            dataSets.add(dataset);
        }
        
        for(BarChartDataSet dataset : dataSets) {
        	data.addChartDataSet(dataset);
        }
      
        List<String> labels = new ArrayList<>();
        labels.add("");

        data.setLabels(labels);
        
        specialtiesBarRag.setData(data);
	}
	
	//SPECIALIZZAZIONI SAF DETTAGLIO
	public void createSpecialtiesSaf() {
		specialtiesSaf = new DonutChartModel();
        ChartData data = new ChartData();
        
        DonutChartDataSet dataSet = new DonutChartDataSet();
        
        Map<String,Number> map = anagraphicService.getSingleSpecialtyCounts("SAF");
        dataSet.setData(new LinkedList<Number>(map.values()));
        
        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgba(255, 99, 132, 0.2)");
        bgColors.add("rgba(255, 159, 64, 0.2)");
        bgColors.add("rgba(255, 205, 86, 0.2)");
        bgColors.add("rgba(75, 192, 192, 0.2)");
        bgColors.add("rgba(54, 162, 235, 0.2)");
        bgColors.add("rgba(153, 102, 255, 0.2)");
        bgColors.add("rgba(201, 203, 207, 0.2)");
        dataSet.setBackgroundColor(bgColors);
        dataSet.setBorderColor(bgColors);
     
        data.addChartDataSet(dataSet);

        data.setLabels(new LinkedList<String>(map.keySet()));
        
        specialtiesSaf.setData(data);
        
	}
	
	
	//SPECIALIZZAZIONI DETTAGLIO
	public void createSpecialtiesBar() {
		specialtiesBar = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Specializzazioni");

        
        Map<String, Number> map = anagraphicService.getSpecialtyCounts();
        barDataSet.setData(new LinkedList<Number>(map.values()));

//        List<String> backgroundColors = new ArrayList<>();
//        List<String> borderColors = new ArrayList<>();
//        int plusSize = specialtyNames.size();
//        if (plusSize > 8) {
//        	backgroundColors = defaultBackgroundColors;
//        	borderColors = defaultBorderColors;
//        	plusSize -= 8;
//        	
//        	int r, g, b;
//        	while (plusSize-- > 0) {
//            	r = (int) (Math.random() * 255);
//            	g = (int) (Math.random() * 255);
//            	b = (int) (Math.random() * 255);
//            	backgroundColors.add("rgba(" + r + ", " + g + ", " + b + ", 0.2)");
//                borderColors.add("rgb(" + r + ", " + g + ", " + b + ")");
//            }
//        } else {
//        	while (plusSize-- > 1) {
//            	backgroundColors.add(defaultBackgroundColors.get(specialtyNames.size() - plusSize));
//                borderColors.add(defaultBorderColors.get(specialtyNames.size() - plusSize));
//            }
//        }
        
//        barDataSet.setBackgroundColor(backgroundColors);
//        barDataSet.setBorderColor(borderColors);
        barDataSet.setBackgroundColor(defaultBackgroundColors.get(0));
        barDataSet.setBorderColor(defaultBorderColors.get(0));
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        data.setLabels(new LinkedList<String>(map.keySet()));
        
        specialtiesBar.setData(data);
	}
	
	public void createQualificationsLine() {
		qualificationsLine = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
       
        Map<String, Number> qualificationCountsMap = anagraphicService.getQualificationCounts();

        dataSet.setData(new LinkedList<>(qualificationCountsMap.values()));
        dataSet.setFill(false);
        dataSet.setLabel("Qualifiche");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        data.setLabels(new LinkedList<>(qualificationCountsMap.keySet()));

        qualificationsLine.setData(data);
	}
	
	//ROUND GRAPH CORRECT
	public void createTurnoAPie() {
		turnoAPie = new PieChartModel();
		ChartData data = new ChartData();
		
		PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(Arrays.asList(anagraphicService.getTurnoACounts()));


        dataSet.setBackgroundColor(defaultBackgroundColors);
        dataSet.setBorderColor(defaultBorderColors);
        dataSet.setBorderWidth(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));

        data.addChartDataSet(dataSet);
        
        List<String> labels = new ArrayList<>();
        labels.add("A1");
        labels.add("A2");
        labels.add("A3");
        labels.add("A4");
        labels.add("A5");
        labels.add("A6");
        labels.add("A7");
        labels.add("A8");
        data.setLabels(labels);

        turnoAPie.setData(data);
	}
	
	public void createTurnoBPie() {
		turnoBPie = new PieChartModel();
		ChartData data = new ChartData();
		
		PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(Arrays.asList(anagraphicService.getTurnoBCounts()));


        dataSet.setBackgroundColor(defaultBackgroundColors);
        dataSet.setBorderColor(defaultBorderColors);
        dataSet.setBorderWidth(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));

        data.addChartDataSet(dataSet);
        
        List<String> labels = new ArrayList<>();
        labels.add("B1");
        labels.add("B2");
        labels.add("B3");
        labels.add("B4");
        labels.add("B5");
        labels.add("B6");
        labels.add("B7");
        labels.add("B8");
        data.setLabels(labels);

        turnoBPie.setData(data);
	}
	
	public void createTurnoCPie() {
		turnoCPie = new PieChartModel();
		ChartData data = new ChartData();
		
		PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(Arrays.asList(anagraphicService.getTurnoCCounts()));


        dataSet.setBackgroundColor(defaultBackgroundColors);
        dataSet.setBorderColor(defaultBorderColors);
        dataSet.setBorderWidth(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));

        data.addChartDataSet(dataSet);
        
        List<String> labels = new ArrayList<>();
        labels.add("C1");
        labels.add("C2");
        labels.add("C3");
        labels.add("C4");
        labels.add("C5");
        labels.add("C6");
        labels.add("C7");
        labels.add("C8");
        data.setLabels(labels);

        turnoCPie.setData(data);
	}
	
	public void createTurnoDPie() {
		turnoDPie = new PieChartModel();
		ChartData data = new ChartData();
		
		PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(Arrays.asList(anagraphicService.getTurnoDCounts()));


        dataSet.setBackgroundColor(defaultBackgroundColors);
        dataSet.setBorderColor(defaultBorderColors);
        dataSet.setBorderWidth(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));

        data.addChartDataSet(dataSet);
        
        List<String> labels = new ArrayList<>();
        labels.add("D1");
        labels.add("D2");
        labels.add("D3");
        labels.add("D4");
        labels.add("D5");
        labels.add("D6");
        labels.add("D7");
        labels.add("D8");
        data.setLabels(labels);

        turnoDPie.setData(data);
	}
	
	public void createTurnoABCDG5DiscPie() {
		turnoABCDG5DiscPie = new PieChartModel();
		ChartData data = new ChartData();
		
		PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(Arrays.asList(anagraphicService.getTurnoABCDG5DiscCounts()));
        dataSet.setBackgroundColor(defaultBackgroundColors);
        dataSet.setBorderColor(defaultBorderColors);
        dataSet.setBorderWidth(Arrays.asList(1, 1, 1, 1, 1, 1));

        data.addChartDataSet(dataSet);
        
        List<String> labels = new ArrayList<>();
        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("G5");
        labels.add("Discontinui");
        data.setLabels(labels);

        turnoABCDG5DiscPie.setData(data);
	}
}
