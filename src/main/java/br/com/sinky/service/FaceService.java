package br.com.sinky.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.sinky.dl4jfacenet.FaceDetector;
import br.com.sinky.dl4jfacenet.model.InceptionResNetV1;

@Component
public class FaceService {

	private  Logger log = LoggerFactory.getLogger(FaceService.class);
	private  NativeImageLoader loader = new NativeImageLoader();
	private  HashMap<String, INDArray> imageLibMap = new HashMap<String, INDArray>();
	private  FaceDetector detector;
	private  ComputationGraph graph;

	public String processarImagem(String path) {
		
		String label = "none";
		///
		System.out.println("start detect");
		/////Scanner sc = new Scanner(System.in);
		/// while(true) {
		try {
		
			File file = new File(path);
			INDArray factor = getFaceFactor(file);
			/*
			 * if(factor == null) { System.out.println("error:cannot detect face.");
			 * continue; }
			 */
			double minVal = Double.MAX_VALUE;
		
			for (Entry<String, INDArray> entry : imageLibMap.entrySet()) {
				INDArray tmp = factor.sub(entry.getValue());
				tmp = tmp.mul(tmp).sum(1);
				double tmpVal = tmp.getDouble(0);
				if (log.isDebugEnabled()) {
					log.debug("current factor is {}", factor);
					log.debug("similarity with {} is {}", entry.getKey(), tmp);
				}
				if (tmpVal < minVal) {
					minVal = tmpVal;
					label = entry.getKey();
				}
			}

			if (minVal < 1.1) {
				System.out.println("this is " + label + "(" + minVal + ").");
			} else {
				System.out
						.println("cannot recognize this person, but the similar one is " + label + "(" + minVal + ").");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		return label;
	}
    @PostConstruct
	public void carregarFotos() {
		try {
			
			String path = new File(".").getCanonicalPath();
			File libPath = new File(path+"/imgLibSample/");
			detector = new FaceDetector();
			InceptionResNetV1 v1 = new InceptionResNetV1();
			v1.init();
			v1.loadWeightData();
			graph = v1.getGraph();
			
			if (!libPath.exists() || !libPath.isDirectory()) {
				throw new RuntimeException("path " + libPath + "is not directory!");
			}
			log.info("loading face library from directory {}, threshold is {}", libPath, 1.1);
			for (File file : libPath.listFiles()) {
				if (!file.isFile()) {
					log.info("skipped directory:{}", file);
					continue;
				}
				try {
					String label = file.getName();
					int labelIdx = label.lastIndexOf('.');
					if (labelIdx != -1) {
						label = label.substring(0, labelIdx);
					}
					log.info("Loading image {}......", file);
					INDArray factor = getFaceFactor(file);
					if (factor == null) {
						continue;
					}

					imageLibMap.put(label, factor);
					log.info("Face of {} loaded.", label);
					if (log.isDebugEnabled()) {
						log.debug("factor of {} is {}", label, factor);
					}
				} catch (IOException e) {
					log.warn("Exception occured while loading img file:" + file, e);
				}
			}
			log.info("load faces complete.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private  INDArray getFaceFactor(File img) throws IOException {
		INDArray srcImg = loader.asMatrix(img);
		INDArray[] detection = detector.getFaceImage(srcImg, 160, 160);
		if (detection == null) {
			log.warn("no face detected in image file:{}", img);
			return null;
		}
		if (detection.length > 1) {
			log.warn("{} faces detected in image file:{}, the first detected face will be used.", detection.length,
					img);
		}
		if (log.isDebugEnabled()) {
			ImageIO.write(detector.toImage(detection[0]), "jpg", new File(img.getName()));
		}
		INDArray output[] = graph.output(InceptionResNetV1.prewhiten(detection[0]));
		return output[1];
	}

}
