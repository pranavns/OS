
//Sleeping barber problem
/*
 The problem is to program the barber and the customers without get-
ting into race conditions
 Solution uses three semaphores:
customers; counts the waiting customers
barbers; the number of barbers (0 or 1)
mutex ; used for mutual exclusion
also need a variable waiting; also counts the waiting customers
(reason; no way to read the current value of semaphore)
 The barber executes the procedure barber, causing him to block
on the semaphore customers (initially 0)
 The barber then goes to sleep
 When a customer arrives, he executes customer, starting by ac-
quiring mutex to enter a critical region
 if another customer enters, shortly thereafter, the second one will
not be able to do anything until the first one has released mutex
*/

#define CHAIRS 5

typedef int semaphore

semaphore customers = 0;
semaphore barbers   = 0;
semaphore mutex	    = 1;
int waiting = 0;

void barber(void)
{
	while(1) 
	{
		down(&customers);
		down(&mutex);
		waiting--;
		up(&barbers);
		up(&mutex);
		cut_hait();
	}
}

void customer(void)
{
	down(&mutex);
	if(waiting<CHAIRS)
	{
		waiting++;
		up(&customers);
		up(&mutex);
		down(&barbers);
		get_haircut();
	}
	else
		up(&mutex);
}

/*
The customer then checks to see if the number of waiting cus-
tomers is less than the number of chairs
– if not, he releases mutex and leaves without a haircut
– if there is an available chair, the customer increments the integer
  variable, waiting
– Then he does an up on the semaphore customers
– When the customer releases mutex, the barber begins the haircut
*/