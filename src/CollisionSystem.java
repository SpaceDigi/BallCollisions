/**
 * Created by SpaceDigi
 * 27.10.2017 at 22:40
 * OKA2
 */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;

public class CollisionSystem {
	private MinPQ<Event> pq;
	private double t  = 0.0;
	private Particle[] particles;


	public CollisionSystem(Particle[] particles) {
		this.particles = particles;
	}


	private void predict(Particle a) {
		if (a == null) return;
		for (int k = 0; k < particles.length; k++) {
			double dt = a.timeToHit(particles[k]);
			pq.insert(new Event(t + dt, a, particles[k]));
		}
			pq.insert(new Event(t + a.timeToHitVerticalWall(), a, null));
			pq.insert(new Event(t + a.timeToHitHorizontalWall(), null, a));
	}


	private void redraw() {
		StdDraw.clear();
		for (int k = 0; k < particles.length; k++) {
			particles[k].draw();
		}
		StdDraw.show(5);
		pq.insert(new Event(t +1, null, null));

	}

	public void simulate() {

		pq = new MinPQ<Event>();
		for (int k = 0; k < particles.length; k++) {
			predict(particles[k]);
		}
		pq.insert(new Event(0, null, null));

		while (!pq.isEmpty()) {
			Event e = pq.delMin();
			if (!e.isValid()) continue;
			Particle a = e.a;
			Particle b = e.b;

			for (int k = 0; k < particles.length; k++)
				particles[k].move(e.time - t);
			t = e.time;

			if      (a != null && b != null) a.bounceOff(b);
			else if (a != null && b == null) a.bounceOffVerticalWall();
			else if (a == null && b != null) b.bounceOffHorizontalWall();
			else if (a == null && b == null) redraw();
			predict(a);
			predict(b);
		}
	}
	private static class Event implements Comparable<Event> {
		private final double time;
		private final Particle a, b;
		private final int countA, countB;


		public Event(double t, Particle a, Particle b) {
			this.time = t;
			this.a    = a;
			this.b    = b;
			if (a != null) countA = a.count();
			else           countA = -1;
			if (b != null) countB = b.count();
			else           countB = -1;
		}

		public int compareTo(Event that) {
			return Double.compare(this.time, that.time);
		}

		public boolean isValid() {
			if (a != null && a.count() != countA) return false;
			if (b != null && b.count() != countB) return false;
			return true;
		}

	}

	public static void main(String[] args) {

		StdDraw.setCanvasSize(600, 600);

		Particle[] particles;

			int n = 100;
			particles = new Particle[n];
			for (int k = 0; k < n; k++) {
				particles[k] = new Particle();
		}
		CollisionSystem cs = new CollisionSystem(particles);
		cs.simulate();
	}

}