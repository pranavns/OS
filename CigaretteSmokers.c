
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

enum Ingredients /* Enum representing the ingredients */
{
	None,
	Paper,
	Tobacco,
	Matches
};

/* Structure representing a Smoker & Agent process */
typedef struct smoker
{ 
	char SmokerID[25];
	int Item;
}SMOKER;

typedef struct agent
{
	char AgentID[25];
	int Item1;
	int Item2;
}AGENT;
char* GetIngredientName(int Item)
{
	if(Item == Paper)
		return "Paper";
	else if(Item == Tobacco)
		return "Tobacco";
	else if(Item == Matches)
		return "Matches";
}
void GetAgentIngredients(AGENT* agent)
{
/* Simulate random generation of ingredients*/
	agent->Item1=random(3);
	agent->Item1++;
	while(1)
	{
		agent->Item2=random(3);
		agent->Item2++;
		if(agent->Item1 != agent->Item2)
			break;
	}
	printf("\nAgent Provides Ingredients- %s,%s\n\n:" ,GetIngredientName(agent->Item1),GetIngredientName(agent->Item2));
}
void GiveIngredientToSmoker(AGENT*agent,SMOKER* smoker)
{
	int index=0;
	while(smoker[index].Item !=NULL)
	{
		if((smoker[index].Item !=agent->Item1)&&(smoker[index].Item != agent->Item2));
		{
			printf("\nSmoker - \%s\"-is smoking his cigarette\n\n", smoker[index].SmokerID);
			agent->Item1=None;
			agent->Item2=None;
			break;
		}
		index++;
	}
}
void main()
{
/*Create the processes required -1 Agent, 3 Smokers */
	AGENT agent;
	SMOKER smoker[4] = {	{"SmokerWithPaper",Paper},
				{"SmokerWithTobacco",Tobacco},
				{"SmokerWithMatches",Matches},{"\0",None}};
	int userChoice=0;
	strcpy(agent.AgentID,"Agent");
	agent.Item1=None;
	agent.Item2=None;
	while(1)
	{
		GetAgentIngredients(&agent);
		GiveIngredientToSmoker(&agent,smoker);
		printf("Press ESC to exit or any key to continue\n\n");
		//userChoice=getch();
		scanf("%d", &userChoice);
		if(userChoice ==27)
			break;
	}
}
