package agglomerative;

import java.util.ArrayList;
import java.util.List;

public class Agnes_2 {
	class Cluster {
		public Cluster() { }
		
		public Cluster(Cluster c) {
			this.cluster1 = c;
		}
		
		public Cluster(Cluster c1, Cluster c2) {
			this.cluster1 = c1;
			this.cluster2 = c2;
		}
		
		public Cluster[] getClusters() {
			Cluster[] clusters = null;
			
			if ( cluster1 != null && cluster2 != null ) {
				clusters = new Cluster[2];
				clusters[0] = cluster1;
				clusters[1] = cluster2;
			} else if ( cluster1 != null ) {
				clusters = new Cluster[1];
				clusters[0] = cluster1;
			} else if ( cluster2 != null ) {
				clusters = new Cluster[1];
				clusters[0] = cluster2;
			}
			return clusters;
		}
		
		public void setCluster(Cluster c) {
			if ( cluster1 == null ) {
				cluster1 = c;
			} else {
				cluster2 = c;
			}
		}
		
		public List<Point> getPoints() {
			List<Point> points = new ArrayList<Point>();
			
			if ( cluster1 instanceof Point ) {
				points.add((Point)cluster1);
			} else if ( cluster1 instanceof Cluster ) {
				points.addAll(cluster1.getPoints());
			}
			if ( cluster2 instanceof Point ) {
				points.add((Point)cluster2);
			} else if ( cluster2 instanceof Cluster ) {
				points.addAll(cluster2.getPoints());
			}
			return points;
		}
		
		public boolean equals(Object o) {
			if ( o instanceof Cluster ) {
				Cluster c = (Cluster)o;
				
				Cluster[] thisClusters = getClusters();
				Cluster[] otherClusters = c.getClusters();
				
				if ( thisClusters.length != otherClusters.length ) {
					return false;
				}
				for ( int i = 0; i < thisClusters.length; ++ i ) {
					if ( !thisClusters[i].equals(otherClusters[i]) ) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("<");
			if ( cluster1 != null ) {
				if ( cluster1 instanceof Point ) {
					sb.append(((Point)cluster1).toString());
				} else {
					sb.append(cluster1.toString());
				}
			}
			if ( cluster2 != null ) {
				sb.append(", ");
				if ( cluster2 instanceof Point ) {
					sb.append(((Point)cluster2).toString());
				} else {
					sb.append(cluster2.toString());
				}
			}
			sb.append(">");
			return sb.toString();
		}
		
		private Cluster cluster1 = null;
		private Cluster cluster2 = null;
	}
	
	class Point extends Cluster {
		public Point(double x, double y, double z, int id) {
			super.cluster1 = this;
			this.x = x;
			this.y = y;
			this.z = z;
			this.id = id;
		}
		
		public String toString() {
			return String.format("(%.2f, %.2f, %.2f)", 
					new Object[] {x, y, z})+"ID_"+id;
		}
		
		public double x;
		public double y;
		public double z;
		public int id;
	}
	
	public Cluster getCluster(List<Cluster> clusters) {
		while ( clusters.size() > 1 ) {
			double minProximity = Double.MAX_VALUE;
			int minProximityIndex1 = 0, minProximityIndex2 = 0;
			System.out.println("--------------------");
			
			for ( int i = 0; i < clusters.size(); ++ i ) {
				for ( int j = i + 1; j < clusters.size(); ++ j ) {
					double proximity = getProximity(clusters.get(i), clusters.get(j));
					
					if ( proximity < minProximity ) {
						minProximity = proximity;
						minProximityIndex1 = i;
						minProximityIndex2 = j;
					}
				}
			}
			System.out.println("minProximity: "+minProximity);
			System.out.println("Point 1: "+clusters.get(minProximityIndex1));
			System.out.println("Point 2: "+clusters.get(minProximityIndex2));
			Cluster c = new Cluster(clusters.get(minProximityIndex1), clusters.get(minProximityIndex2));
			clusters.add(c);

			clusters.remove(minProximityIndex2);
			clusters.remove(minProximityIndex1);
		}
		return clusters.size() == 0 ? null : clusters.get(0);
	}
	
	private double getProximity(Cluster cluster1, Cluster cluster2) {
		List<Point> points1 = cluster1.getPoints();
		List<Point> points2 = cluster2.getPoints();
		double totalDistance = 0;
		
		for ( int i = 0; i < points1.size(); ++ i ) {
			for ( int j = 0; j < points2.size(); ++ j ) {
				totalDistance += getDistance(points1.get(i), points2.get(j));
			}
		}
		return totalDistance / (points1.size() * points2.size());
	}
	
	private double getDistance(Point p1, Point p2) {
		double x = Math.pow(Math.abs(p2.x - p1.x),3);
		double y = Math.pow(Math.abs(p2.y - p1.y),3);
		double z = Math.pow(Math.abs(p2.z - p1.z),3);
		double sum = x + y + z;
		double result =  Math.cbrt(sum);
		return result;
	}
	
	public static void main(String[] args) {
		Agnes_2 agnes = new Agnes_2();
		
		List<Cluster> points = new ArrayList<Cluster>();
		System.out.println("Original Points: ");

		Cluster p1 = agnes.new Point(5,1,2,1);
		points.add(p1);
		System.out.println(p1);
		Cluster p2 = agnes.new Point(9,1,2,2);
		points.add(p2);
		System.out.println(p2);
		Cluster p3 = agnes.new Point(3,2,2,3);
		points.add(p3);
		System.out.println(p3);
		Cluster p4 = agnes.new Point(7,2,2,4);
		points.add(p4);
		System.out.println(p4);
		Cluster p5 = agnes.new Point(8,1,2,5);
		points.add(p5);
		System.out.println(p5);
		Cluster p6 = agnes.new Point(5,1,1,6);
		points.add(p6);
		System.out.println(p6);
		Cluster p7 = agnes.new Point(6,1,2,7);
		points.add(p7);
		System.out.println(p7);
		Cluster p8 = agnes.new Point(4,2,1,8);
		points.add(p8);
		System.out.println(p8);
		Cluster p9 = agnes.new Point(10,3,2,9);
		points.add(p9);
		System.out.println(p9);
		Cluster p10 = agnes.new Point(10,3,2,10);
		points.add(p10);
		System.out.println(p10);
		Cluster p11 = agnes.new Point(10,3,2,11);
		points.add(p11);
		System.out.println(p11);
		Cluster p12 = agnes.new Point(2,3,1,12);
		points.add(p12);
		System.out.println(p12);
		Cluster p13 = agnes.new Point(6,1,2,13);
		points.add(p13);
		System.out.println(p13);
		Cluster p14 = agnes.new Point(5,1,2,14);
		points.add(p14);
		System.out.println(p14);
		Cluster p15 = agnes.new Point(10,3,2,15);
		points.add(p15);
		System.out.println(p15);
		Cluster p16 = agnes.new Point(10,3,2,16);
		points.add(p16);
		System.out.println(p16);
		Cluster p17 = agnes.new Point(10,3,2,17);
		points.add(p17);
		System.out.println(p17);		
		Cluster p18 = agnes.new Point(3,2,2,18);
		points.add(p18);
		System.out.println(p18);
		Cluster p19 = agnes.new Point(3,2,2,19);
		points.add(p19);
		System.out.println(p19);
		Cluster p20 = agnes.new Point(10,3,2,20);
		points.add(p20);
		System.out.println(p20);
		Cluster p21 = agnes.new Point(10,3,2,21);
		points.add(p21);
		System.out.println(p21);
		Cluster p22 = agnes.new Point(10,3,1,22);
		points.add(p22);
		System.out.println(p22);
		Cluster p23 = agnes.new Point(1,2,2,23);
		points.add(p23);
		System.out.println(p23);
		
		System.out.println();
		
		System.out.println("Clusters: ");
		Cluster c = agnes.getCluster(points);
		System.out.println("One Cluster: ");
		System.out.println(c);
	}
}