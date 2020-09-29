# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"

        closestGhostDist = min([manhattanDistance(ghost.configuration.getPosition(),newPos) for ghost in  newGhostStates])
        foods = newFood.asList()
        score = 0
        if len(foods)==0:
            return float('inf')
        closestfooddist = min([manhattanDistance(food, newPos) for food in foods])
        score += 10000/(len(foods)+1)


        if closestGhostDist <= 2:
            score /= 10
        score += 1/closestfooddist
        
        
        return score
def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        
        self.wanted_action = gameState.getLegalActions(0)[0]


        def maxhelper(depthnow,gameState, agentIndex):
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow,self.depth,gameState.state,self.evaluationFunction(gameState))

                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                max_results = -1* float('inf')
                max_action  = None
                
                for action in actionable:
                    #print(depthnow, gameState.state)
                    successorstate = gameState.generateSuccessor(agentIndex,action)
                    mh = minhelper(depthnow,successorstate,1)
                    max_results = max(max_results,mh)
                    if  mh == max_results:
                        max_action = action        
                self.wanted_action = max_action
                return max_results
        def minhelper(depthnow, gameState, agentIndex):
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow)
                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                succ = [gameState.generateSuccessor(agentIndex,action) for action in actionable]
                result = []
                for successorstate in succ:
                    #print(depthnow, gameState.state)
                    if agentIndex< gameState.getNumAgents()-1:
                        result.append(minhelper(depthnow,successorstate,agentIndex+1))
                    else:
                        result.append(maxhelper(depthnow+1,successorstate,0))
                    
                return min(result)
        
        maxhelper(0,gameState,0)
        return self.wanted_action



class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        
     
        self.action_wanted = None
        def maxipad(depthnow,gameState,agentIndex,alpha,beta):
            #print(alpha,beta,gameState.state)
        
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow,self.depth,gameState.state,self.evaluationFunction(gameState))

                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                max_results = -1* float('inf')
                max_action  = None
                
                for action in actionable:
                    
                    #print(depthnow, gameState.state)
                    successorstate = gameState.generateSuccessor(agentIndex,action)
                    mh = minipad(depthnow,successorstate,1,alpha,beta)
                
                    if mh > beta:
                        self.action_wanted = action
                        return mh
                    else:
                        alpha = max(alpha, mh)
                    #print()
                    #print(max_results,mh)
                    max_results = max(max_results,mh)
                    #print(max_results,mh)
                    if  mh == max_results:
                        #print(2)
                        max_action = action 
                        #print(max_action)
                
                #print(max_results)       
                self.action_wanted = max_action
                return max_results

        def minipad(depthnow,gameState,agentIndex,alpha,beta):
            #print(alpha,beta,gameState.state)
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow)
                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                result = []
                for action in actionable:
                    successorstate = gameState.generateSuccessor(agentIndex, action)
                    #print(result)
                    #print(depthnow, gameState.state)
                    if agentIndex< gameState.getNumAgents()-1:
                        mh = minipad(depthnow,successorstate,agentIndex+1,alpha,beta)
                        if mh < alpha:
                            return mh
                        else:
                            beta = min(mh,beta)
                        result.append(mh)
                    else:
                        mh = maxipad(depthnow+1,successorstate,0,alpha,beta)
                        if mh < alpha:
                            return mh
                        else:
                            beta = min(mh,beta)
                        result.append(mh)
                #print(result) 
                return min(result)
        maxipad(0,gameState,0,-1*float('inf'),float('inf'))
        return self.action_wanted
            

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        self.wanted_action = gameState.getLegalActions(0)[0]


        def maxhelper(depthnow,gameState, agentIndex):
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow,self.depth,gameState.state,self.evaluationFunction(gameState))

                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                max_results = -1* float('inf')
                max_action  = None
                
                for action in actionable:
                    #print(depthnow, gameState.state)
                    successorstate = gameState.generateSuccessor(agentIndex,action)
                    mh = expectopatronum(depthnow,successorstate,1)
                    max_results = max(max_results,mh)
                    if  mh == max_results:
                        max_action = action        
                self.wanted_action = max_action
                return max_results
        def expectopatronum(depthnow, gameState, agentIndex):
            if (depthnow == self.depth) or gameState.isWin() or gameState.isLose():
                #print(depthnow)
                return self.evaluationFunction(gameState)
            else:
                actionable = gameState.getLegalActions(agentIndex)
                succ = [gameState.generateSuccessor(agentIndex,action) for action in actionable]
                result = []
                for successorstate in succ:
                    #print(depthnow, gameState.state)
                    if agentIndex< gameState.getNumAgents()-1:
                        result.append(expectopatronum(depthnow,successorstate,agentIndex+1))
                    else:
                        result.append(maxhelper(depthnow+1,successorstate,0))
                    
                return sum(result)/len(succ)
        
        maxhelper(0,gameState,0)
        return self.wanted_action

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    
    newPos = currentGameState.getPacmanPosition()
    foods = currentGameState.getFood().asList()
    newGhostStates = currentGameState.getGhostStates()
    newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]



    closestGhostDist = min([manhattanDistance(ghost.configuration.getPosition(),newPos) for ghost in  newGhostStates])
    i= 0
    j= 0
    for ghost in newGhostStates:
        if manhattanDistance(ghost.configuration.getPosition(),newPos) == closestGhostDist:
            j = i
        i+=1

    score = 0
    if len(foods)==0:
        return float('inf')
    closestfooddist = min([manhattanDistance(food, newPos) for food in foods])
    score += 10000/(len(foods)+1)

    if closestGhostDist <= newScaredTimes[j]:
                score += closestGhostDist*100
    if closestGhostDist <= 2:
        score /= 10
    score += 0.5 *1/closestfooddist
        
        
    return score
    

# Abbreviation
better = betterEvaluationFunction
