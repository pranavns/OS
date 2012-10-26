//Dining Philosophers Problem

/*
	5 Philosophers are given one table to sit and there are
	5 plates full of food to eat. And in between the 2 consecutive
	plates one forks are provided. So total forks are five.
	philosopher may be in 3 states... thinking, eating, and waiting
	for the fork.
	The problem is two consecutive phils cannot be eat simultaneouly 
	but the next Phil can wait until the eating is completed...
	the philosophers wants to eat and think at equal interval of time

*/ 

import java.io.*;

class lock{}	//this is not necessary since Table's object also performs monitor lock

class Table extends Thread {
	int phil_States[] = new int[5]; 	// 0=thinking, 1=waiting, 2=eating
    	boolean fork_States[] = new boolean[5]; // forks:- false = in use, true = free
	lock L = new lock();			//dummy class object to acts as monitor-lock
	Table() {
 		for(int i=0;i<5;i++) {
    			phil_States[i]=0;	//initially phils are thinking.
    			fork_States[i]=true;	//all forks are free
    		}
    	}
	void print_State() {
		synchronized(L) {
    				for(int i=0;i<5;i++) {	//print all phil states
					if(phil_States[i]==0)
    						System.out.print("think\t");
					else if(phil_States[i]==1)
						System.out.print("wait\t");
					else
						System.out.print("eat\t");
				}
				System.out.print("\n");
		}
    	}
    	void ask_to_eat(int pId) {
		synchronized(L) {
	    		while(!fork_States[pId] || !fork_States[(pId+1)%4]){ 	// while it can't have both forks, wait
    				phil_States[pId] = 1;
    				try {L.wait();} catch(InterruptedException e) {} // it gets released grabs the lock from current process
			}
    		} 				//by a process doing a call to notify()
    		phil_States[pId] = 2; 		//eating because both(left,right) forks are free
    		fork_States[pId] = false; 	//left fork in use
    		fork_States[(pId+1)%4] = false; //right fork in use
    	}
    	void ask_to_leave(int pId) {
		synchronized(L) {
	    		fork_States[pId] = true; 	// left fork is available
	    		fork_States[(pId+1)%4] = true;	//right fork is available
	    		phil_States[pId] = 0; 		// thinking satatee
	    		L.notify(); 			// free the Phil that has waited the longest by releasing the lock
		}
    	}
}

class Philo extends Thread
{
	Table m;	
    	int pId;
    	Philo(int pId, Table t) {
    		System.out.println("\n" + pId + "is started: ");
    		this.pId = pId;
    		this.m = t;	//shared object table
	}
    	public void run() { 	//overriding the run()	
		while(true) {	//philosophers doing these acivites infinitly 
			
    			m.ask_to_eat(pId);
    			try {Thread.sleep(500);} catch(Exception e){}	//5milli seconds to eat
			m.print_State();
    			try {Thread.sleep(500);} catch(Exception e){}	//5ms to print the state
			m.ask_to_leave(pId);
    			try {Thread.sleep(500);} catch(Exception e){}	//5milli second to leave the fork
			m.print_State();
    			try {Thread.sleep(500);} catch(Exception e){}	//5ms to print the state
			
    		}
    	}
}

class DiningPhilosophers
{
	public static void main(String[] args)
	{
		int i;
		Table t  = new Table();		//one table object is given to 5 philosophers
		Philo p[]= new Philo[5]; 	// make an array of 5 refs to Phils
    		for(i=0; i<5; i++)
    			p[i] = new Philo(i,t);
		for(i=0;i<5;i++)
			p[i].start();
	}
}