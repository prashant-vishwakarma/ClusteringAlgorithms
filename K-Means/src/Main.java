package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
	public static void main(String args[]) throws IOException {
		FileReader input = new FileReader("points.txt");
		BufferedReader br = new BufferedReader(input);
		double points[][], centroids[][], distances[][];
		int clusterCounts[], clusters[][];
		int count = 0, k = 2;
		double minX = 1000, minY = 1000, maxX = 0, maxY = 0;
		double minDistance = 1000;

		String temp;
		while ((temp = br.readLine()) != null) {
			count++;
		}

		input = new FileReader("points.txt");
		br = new BufferedReader(input);
		points = new double[count][2];
		int i = 0;

		temp = "";
		while ((temp = br.readLine()) != null) {
			String[] x = temp.split("[,]");
			// System.out.println(temp);
			points[i][0] = Double.parseDouble(x[0]);
			minX = points[i][0] < minX ? points[i][0] : minX;
			maxX = points[i][0] > maxX ? points[i][0] : maxX;

			points[i][1] = Double.parseDouble(x[1]);
			minY = points[i][1] < minY ? points[i][1] : minY;
			maxY = points[i][1] > maxY ? points[i][1] : maxY;
			i++;
		}

		br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Number of Clusters");
		k = Integer.parseInt(br.readLine());
		centroids = new double[k][2];
		distances = new double[k][count];
		clusters = new int[k][count];
		clusterCounts = new int[k];

		Random random = new Random();

		// random centroids
		for (i = 0; i < k; i++) {
			centroids[i][0] = random.nextInt((int) ((maxX - minX) + 1)) + minX;
			centroids[i][1] = random.nextInt((int) ((maxY - minY) + 1)) + minY;
			// System.out.println("Centroid#" + (i + 1) + ": " + centroids[i][0] + "," +
			// centroids[i][1]);
		}

		printCentroids(centroids, k);

		// calculate distances
		/*
		 * for (i = 0; i < k; i++) { for (int j = 0; j < count; j++) { distances[i][j] =
		 * argument(centroids[i][0], centroids[i][1], points[j][0], points[j][1]); } }
		 */
		distances = calculateDistances(centroids, points, k, count);

		int minI = 0;

		// findMinimum I
		for (int j = 0; j < count; j++) {
			for (i = 0; i < k; i++) {
				if (i == 0) {
					minDistance = distances[i][j];
					minI = 0;
				}
				if (minDistance > distances[i][j]) {
					minDistance = distances[i][j];
					minI = i;
				}
			}
			clusters[minI][clusterCounts[minI]++] = j;// build cluster
		}

		centroids = calculateCentroids(clusters, clusterCounts, k, centroids);

		printCentroids(centroids, k);

		System.out.println("End Reached");
	}// end of main
	
	static int[][] buildClusters(double[][] distances,int count, int k){
		double minDistance = 1000;
		int[][] clusters = new int[k][count];
		int[] clusterCounts = new int[k];
		for (int j = 0; j < count; j++) {
			int minI = 0;
			for (int i = 0; i < k; i++) {
				if (i == 0) {
					minDistance = distances[i][j];
					minI  = 0;
				}
				if (minDistance > distances[i][j]) {
					minDistance = distances[i][j];
					minI = i;
				}
			}
			clusters[minI][clusterCounts[minI]++] = j;// build cluster
		}
		return clusters;
	}

	static double argument(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
	}

	static void printCentroids(double centroids[][], int k) {
		System.out.println("");
		for (int i = 0; i < k; i++) {
			System.out.println("Centroid#" + (i + 1) + ": " + centroids[i][0] + "," + centroids[i][1]);
		}
	}

	static double[][] calculateDistances(double centroids[][], double points[][], int k, int count) {
		double[][] distances = new double[k][count];
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < count; j++) {
				distances[i][j] = argument(centroids[i][0], centroids[i][1], points[j][0], points[j][1]);
			}
		}
		return distances;
	}

	static double[][] calculateCentroids(int[][] clusters, int[] clusterCounts, int k, double[][] prevCentroids) {
		double[][] centroids = new double[k][2];
		for (int i = 0; i < k; i++) {
			centroids[i][0] = prevCentroids[i][0];
			centroids[i][1] = prevCentroids[i][1];
		}
		for (int i = 0; i < k; i++) {
			if (clusterCounts[i] != 0) {
				int sumX = 0;
				int sumY = 0;
				for (int j = 0; j < clusterCounts[i]; j++) {
					sumX += clusters[i][0];
					sumY += clusters[i][1];
				}
				sumX /= clusterCounts[i];
				sumY /= clusterCounts[i];
				centroids[i][0] = sumX;
				centroids[i][1] = sumY;
			}
		}
		return centroids;
	}
	

}
