import nn

class PerceptronModel(object):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.
        """
        self.w = nn.Parameter(1, dimensions)

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)
        """
        return nn.DotProduct( self.get_weights() ,x)

    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        dotprodfloat = nn.as_scalar(self.run(x))  
        if dotprodfloat >= 0:
            return 1
        else:
            return -1
    

    def train(self, dataset):
        """
        Train the perceptron until convergence.
        """
        while True:
            stop = True
            for x,y in dataset.iterate_once(1):
                prediction = self.get_prediction(x)
                if prediction != nn.as_scalar(y):
                    stop = False
                    self.w.update(x,nn.as_scalar(y))
            if stop:
                break

class RegressionModel(object):
    """
    A neural network model for approximating a function that maps from real
    numbers to real numbers. The network should be sufficiently large to be able
    to approximate sin(x) on the interval [-2pi, 2pi] to reasonable precision.
    """
    def __init__(self):
        # Initialize your model parameters here
        self.w1 = nn.Parameter(1,40)
        self.b1 = nn.Parameter(1,40)
        self.w2 = nn.Parameter(40,1)
        self.b2 = nn.Parameter(1,1)
        self.parameters = [self.w1,self.b1,self.w2,self.b2]
        self.learn = 0.002

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
        Returns:
            A node with shape (batch_size x 1) containing predicted y-values
        """
        first_lin = nn.Linear(x,self.w1)
        first_lay = nn.AddBias(first_lin,self.b1)
        non_lin = nn.ReLU(first_lay)
        second_lin = nn.Linear(non_lin, self.w2)
        second_lay = nn.AddBias(second_lin,self.b2)
        return second_lay

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
            y: a node with shape (batch_size x 1), containing the true y-values
                to be used for training
        Returns: a loss node
        """
        predic = self.run(x)
        loss = nn.SquareLoss(predic,y)
        return loss

    def train(self, dataset):
        """
        Trains the model.
        """
        while True:
            for x,y in dataset.iterate_once(50):
                loss = self.get_loss(x,y)
                gradient = nn.gradients(loss,self.parameters)
                for parameter in range(len(self.parameters)):
                    thisParameter = self.parameters[parameter]
                    thisParameter.update(gradient[parameter],-self.learn)
            for x,y in dataset.iterate_once(len(dataset.x)):
                lossBoy = nn.as_scalar(self.get_loss(x,y))
                if lossBoy < 0.02:
                    return

class DigitClassificationModel(object):
    """
    A model for handwritten digit classification using the MNIST dataset.

    Each handwritten digit is a 28x28 pixel grayscale image, which is flattened
    into a 784-dimensional vector for the purposes of this model. Each entry in
    the vector is a floating point number between 0 and 1.

    The goal is to sort each digit into one of 10 classes (number 0 through 9).

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Initialize your model parameters here
        "*** YOUR CODE HERE ***"
        self.dubs = nn.Parameter(784,200)
        self.b = nn.Parameter(1,200)
        self.moredubs = nn.Parameter(200,10)
        self.b1 = nn.Parameter(1,10)
        self.parameters = [self.dubs,self.b,self.moredubs,self.b1]
        self.learn = 0.0047




    def run(self, x):
        """
        Runs the model for a batch of examples.

        Your model should predict a node with shape (batch_size x 10),
        containing scores. Higher scores correspond to greater probability of
        the image belonging to a particular class.

        Inputs:
            x: a node with shape (batch_size x 784)
        Output:
            A node with shape (batch_size x 10) containing predicted scores
                (also called logits)
        """
        first_lin = nn.Linear(x,self.dubs)
        first_lay = nn.AddBias(first_lin,self.b)
        non_lin = nn.ReLU(first_lay)
        second_lin = nn.Linear(non_lin, self.moredubs)
        second_lay = nn.AddBias(second_lin,self.b1)
        return second_lay
        

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss node
        """
        predic = self.run(x)
        loss = nn.SoftmaxLoss(predic,y)
        return loss

    def train(self, dataset):
        """
        Trains the model.
        """
        while True:
            for x,y in dataset.iterate_once(50):
                loss = self.get_loss(x,y)
                gradient = nn.gradients(loss,self.parameters)
                for parameter in range(len(self.parameters)):
                    thisParameter = self.parameters[parameter]
                    thisParameter.update(gradient[parameter],-self.learn)
            if dataset.get_validation_accuracy() >= 0.975:
                return

class LanguageIDModel(object):
    """
    A model for language identification at a single-word granularity.

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Our dataset contains words from five different languages, and the
        # combined alphabets of the five languages contain a total of 47 unique
        # characters.
        # You can refer to self.num_chars or len(self.languages) in your code
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]

        # Initialize your model parameters here
        self.learn = .09
        self.i_w = nn.Parameter(self.num_chars, 300)
        self.i_b = nn.Parameter(1, 300)
        self.x_w = nn.Parameter(self.num_chars, 300)
        self.h_w = nn.Parameter(300, 300)
        self.b = nn.Parameter(1, 300)
        self.output_w = nn.Parameter(300, len(self.languages))
        self.output_b = nn.Parameter(1, len(self.languages))
        self.parameters = [self.i_w, self.i_b, self.x_w, self.h_w, self.b, self.output_w, self.output_b]

    def run(self, xs):
        """
        Runs the model for a batch of examples.

        Although words have different lengths, our data processing guarantees
        that within a single batch, all words will be of the same length (L).

        Here `xs` will be a list of length L. Each element of `xs` will be a
        node with shape (batch_size x self.num_chars), where every row in the
        array is a one-hot vector encoding of a character. For example, if we
        have a batch of 8 three-letter words where the last word is "cat", then
        xs[1] will be a node that contains a 1 at position (7, 0). Here the
        index 7 reflects the fact that "cat" is the last word in the batch, and
        the index 0 reflects the fact that the letter "a" is the inital (0th)
        letter of our combined alphabet for this task.

        Your model should use a Recurrent Neural Network to summarize the list
        `xs` into a single node of shape (batch_size x hidden_size), for your
        choice of hidden_size. It should then calculate a node of shape
        (batch_size x 5) containing scores, where higher scores correspond to
        greater probability of the word originating from a particular language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
        Returns:
            A node with shape (batch_size x 5) containing predicted scores
                (also called logits)
        """
        "*** YOUR CODE HERE ***"
        hach = nn.Linear(xs[0],self.i_w)
        hach = nn.AddBias(hach,self.i_b)
        h = nn.ReLU(hach)
        for x in xs[1:]:
            h = nn.ReLU(nn.AddBias(nn.Add(nn.Linear(x, self.x_w),nn.Linear(h, self.h_w)), self.b))
        return nn.AddBias(nn.Linear(h, self.output_w), self.output_b)

    def get_loss(self, xs, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 5). Each row is a one-hot vector encoding the correct
        language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
            y: a node with shape (batch_size x 5)
        Returns: a loss node
        """
        "*** YOUR CODE HERE ***"
        return nn.SoftmaxLoss(self.run(xs), y)

        

    def train(self, dataset):
        """
        Trains the model.
        """
        "*** YOUR CODE HERE ***"
        while True:
            for x,y in dataset.iterate_once(50):
                loss = self.get_loss(x,y)
                gradient = nn.gradients(loss,self.parameters)
                for parameter in range(len(self.parameters)):
                    thisParameter = self.parameters[parameter]
                    thisParameter.update(gradient[parameter],-self.learn)
            if dataset.get_validation_accuracy() >= 0.865:
                return