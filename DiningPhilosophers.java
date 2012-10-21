//Dining Philosophers Problem

import java.util.Random;

class Monitor {
	int phil_States[] = new int[5]; // 0=not_waiting, 1=waiting, 2=eating
    	boolean fork_States[] = new boolean[5]; // false = in use, true = free
	Monitor() { // constructor
 		for(int i=0;i<5;i++) {
    			phil_States[i]=0;
    			fork_States[i]=true;
    		}
    	}
	synchronized void print_State() {
    		System.out.println(); // newline
    			for(int i=0;i<5;i++)
    				System.out.print(" " + phil_States[i]);
    	}
    	synchronized void ask_to_eat(int pId) {
    		while(!fork_States[pId] || !fork_States[(pId+1)%4])
    		{ // while it can't have both forks, wait
    			phil_States[pId] = 1;
    			try {wait();} catch(InterruptedException e) {} // it gets released
    		} // by a process doing a call to notify()
    		phil_States[pId] = 2; // eating
    		fork_States[pId] = false; // left fork in use
    		fork_States[(pId+1)%4] = false; //right fork in use
    	}
    	synchronized void ask_to_leave(int pId) {
    		fork_States[pId] = true; // left fork is available
    		fork_States[(pId+1)%4] = true;	//right fork is available
    		phil_States[pId] = 0; // thinking
    		notify(); // free the Phil that has waited the longest
    	}
}

class Timer implements Runnable {
	Monitor m;
	int completed;
	Timer(Monitor m) { // constructor
		this.m = m;
		new Thread(this, "Tim").start(); // make a new thread and start it
    		completed=0;
    	}
    	public void report_Stop() {
    		completed++;
    	}
    	public void run() { // must override run(), this is
    		while(completed!=5) { // what happens when the thread
    			m.print_State(); // begins
    			try {Thread.sleep(500);} catch(Exception e){}
    		}
    	}
}

class Phil implements Runnable {
	Monitor m;
    	Timer t;
    	Random r = new Random(); // Random number generator object
    	int pId;
    	float time;
    	Phil(int pId, Monitor m, Timer t) { // constructor
    		System.out.println("\n" + pId + "is started: ");
    		this.pId = pId;
    		this.m = m;
    		this.t = t;
    		new Thread(this, "Phil").start(); // make a new thread and start it
	}
    	public void run() { // must override run, this is what
    		for(int i=0; i<20; i++) { // is executed when the thread starts
    			m.ask_to_eat(pId); // running
    			time = 1000*r.nextFloat();
    			try {Thread.sleep((int)time);} catch(Exception e){}
    			m.ask_to_leave(pId);
    			time = 1000*r.nextFloat();
    			try {Thread.sleep((int)time);} catch(Exception e){}
    		}
    		t.report_Stop(); // tell the timer this one is done
    	}
}

class Diners {
	public static void main(String args[]) { // execution of the whole
    		Monitor m = new Monitor(); // thing begins here
    		Timer t = new Timer(m); // make a new timer
    		Phil p[] = new Phil[5]; // make an array of 5 refs to Phils
    		for(int i=0; i<5; i++)
    			p[i] = new Phil(i,m,t); // create the phils and start them
    	}
}
