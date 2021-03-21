

class CoolHeap {

	private class Node{
		public Integer data; //το περιεχόμενο του κόμβου στο σωρό
		public int positionInMaxHeap; // η θέση στο σωρό μεγίστου
		public int positionInMinHeap; // η θέση στο σωρό ελαχίστου
		public int positionInAMN; // // η θέση στον πίνακα όλων των κόμβων
		// τα 3 τελευταια πεδία λειτουργούν ως ευρετήριο δίνοντας μου τις θέσεις του κάθε στοιχείου ανα πάσα στιγμή.
	}


	private int maxSize;
	private int currentSize;

	private Node [] maxHeap; // πίνακας που περιέχει τους κόμβους του σωρού μεγίστου
	private Node [] minHeap; // πίνακας που περιέχει τους κόμβους του σωρού ελαχίστου
	private Node [] allMyNodes; // ένας πίνακας που περιέχει όλους τους κόμβους που έχουν εισαχθεί



	public CoolHeap(int mx) {
		maxHeap=new Node[mx];
		minHeap=new Node[mx];
		allMyNodes=new Node[mx];
		maxSize=mx;
		currentSize=0;
		}

	private boolean isEmpty() {
		if (currentSize!=0)
			return false;
		return true;
	}

	public Integer insertMax(int key) {
		if(currentSize < maxSize){ //there is empty space

			Integer newKey = new Integer(key);
			Node newNode = new Node();
			newNode.data = newKey; // δημιουργία νέου κόμβου

			maxHeap[currentSize] = newNode; // εισαγωγή σε σωρό
			int pos = trickleupForMaxHeap(currentSize); // αποκατάσταση ιδιότητας σωρού
			newNode.positionInMaxHeap = pos; // ενημέρωση "ευρετηρίου"

			minHeap[currentSize] = newNode; // ομοιως αλλα για τον σωρό ελαχίστου
			pos = trickleupForMinHeap(currentSize);
			newNode.positionInMinHeap = pos;

			newNode.positionInAMN = currentSize; // ενημέρωση "ευρετηρίου"
			allMyNodes[currentSize++] = newNode; // εισαγωγή νέου κόμβου στο πίνακα όλων των κόμβων.

			return null;
		}
		else
		{
			if(key < minHeap[0].data.intValue()) {
				return new Integer(key);
			}
			else {

				Integer toReturn = removeMin();

				Integer newKey = new Integer(key);
				Node newNode = new Node();
				newNode.data = newKey;

				maxHeap[currentSize] = newNode;
				int pos = trickleupForMaxHeap(currentSize);
				newNode.positionInMaxHeap = pos;

				minHeap[currentSize] = newNode;
				pos = trickleupForMinHeap(currentSize);
				newNode.positionInMaxHeap = pos;

				newNode.positionInAMN = currentSize;
				allMyNodes[currentSize++] = newNode;

				return toReturn;
			}
		}
	}

	public Integer insertMin(int key) {
		if(currentSize < maxSize){ //there is empty space

			Integer newKey = new Integer(key);
			Node newNode = new Node();
			newNode.data = newKey;

			maxHeap[currentSize] = newNode;
			int pos = trickleupForMaxHeap(currentSize);
			newNode.positionInMaxHeap = pos;

			minHeap[currentSize] = newNode;
			pos = trickleupForMinHeap(currentSize);
			newNode.positionInMinHeap = pos;

			newNode.positionInAMN = currentSize;
			allMyNodes[currentSize++] = newNode;

			return null;
		}
		else{
			if(key > maxHeap[0].data.intValue()) {
				return new Integer(key);
			}
			else {

				Integer toReturn = removeMax();

				Integer newKey = new Integer(key);
				Node newNode = new Node();
				newNode.data = newKey;

				maxHeap[currentSize] = newNode;
				int pos = trickleupForMaxHeap(currentSize);
				newNode.positionInMaxHeap = pos;

				minHeap[currentSize] = newNode;
				pos = trickleupForMinHeap(currentSize);
				newNode.positionInMinHeap = pos;

				newNode.positionInAMN = currentSize;
				allMyNodes[currentSize++] = newNode;

				return toReturn;
			}
		}
	}

	public Integer removeMax() {
		if(isEmpty()) {
			return null;
		} else {
			Integer removedKey = maxHeap[0].data;

			Node lastMax = maxHeap[0]; // αποθηκευω το παλιο μεγιστο στοιχείο για αργοτερα
			maxHeap[0] = maxHeap[currentSize-1]; // θετω ρίζα το τελευταιο στοιχείο του τελευταίου επιπέδου
			allMyNodes[maxHeap[0].positionInAMN].positionInMaxHeap = 0; // ενημερώνω το ευρετήριο μου
			trickledownForMaxHeap(0); // επαναφέρω την κατασταση σωρου.

			int lastMaxPos = allMyNodes[lastMax.positionInAMN].positionInMinHeap; // βρισκω τη θέση του παλιου μεγιστου στο σωρο ελαχίστου
			minHeap[lastMaxPos] = minHeap[--currentSize]; //διαγραφω το παλιο στοιχειο απο το σωρο ελαχιστου (θα βρίσκεται στα φύλλα)
			allMyNodes[minHeap[lastMaxPos].positionInAMN].positionInMinHeap = lastMaxPos; // ενημερώνω το ευρετήριο μου
			trickleupForMinHeap(lastMaxPos); // επαναφέρω την κατασταση σωρου.

			return removedKey;

		}


	}

	public Integer removeMin() {
		if(isEmpty()) {
			return null;
		} else {
			Integer removedKey = minHeap[0].data;

			Node lastMin = minHeap[0];
			minHeap[0] = minHeap[currentSize-1];
			allMyNodes[minHeap[0].positionInAMN].positionInMinHeap = 0;
			trickledownForMinHeap(0);

			int lastMinPos = allMyNodes[lastMin.positionInAMN].positionInMaxHeap;
			maxHeap[lastMinPos] = maxHeap[--currentSize];
			allMyNodes[maxHeap[lastMinPos].positionInAMN].positionInMaxHeap = lastMinPos;
			trickleupForMaxHeap(lastMinPos);

			return removedKey;

		}
	}



    private int trickleupForMaxHeap(int index){ //same as lecture notes
    	int parent = (index-1)/2;
    	Node bottom = maxHeap[index];

    	while (index > 0 &&
    			maxHeap[parent].data.intValue() < bottom.data.intValue()) {

    		maxHeap[index] = maxHeap[parent]; // move node down
    		allMyNodes[maxHeap[parent].positionInAMN].positionInMaxHeap = index; //ενημέρωση ευρετηρίου
    		index = parent;
    		parent = (parent-1)/2;
    	}
    	maxHeap[index]=bottom;
    	return index;
    }

    private int trickleupForMinHeap(int index){ //same as lecture notes. Ομοίως με πάνω αλλά για το σωρό ελαχίστου
    	int parent = (index-1)/2;
    	Node bottom = minHeap[index];

    	while (index > 0 &&
    			minHeap[parent].data.intValue() > bottom.data.intValue()) {

    		minHeap[index] = minHeap[parent];
    		allMyNodes[minHeap[parent].positionInAMN].positionInMinHeap = index;
    		index = parent;
    		parent = (parent-1)/2;
    	}
    	minHeap[index]=bottom;
    	return index;
    }

    private void trickledownForMaxHeap(int index){ //same as lecture notes

    	int largerChild;
    	Node top = maxHeap[index];
    	while(index < currentSize/2) {

    		int leftChild = 2 * index +1;
    		int rightChild = leftChild + 1;

    		if(rightChild < currentSize &&
    				maxHeap[leftChild].data.intValue() < maxHeap[rightChild].data.intValue()) {
    			largerChild = rightChild;
    		}else {
    			largerChild = leftChild;
    		}

    		if (top.data.intValue() >= maxHeap[largerChild].data.intValue())
    			break;


    		maxHeap[index] = maxHeap[largerChild];
    		allMyNodes[maxHeap[largerChild].positionInAMN].positionInMaxHeap = index;
    		index = largerChild;
    	}
    	maxHeap[index] = top;
    	allMyNodes[top.positionInAMN].positionInMaxHeap = index;

    }

    private void trickledownForMinHeap(int index){

    	int smallerChild;
    	Node top = minHeap[index];
    	while(index < currentSize/2) {

    		int leftChild = 2 * index +1;
    		int rightChild = leftChild + 1;

    		if(rightChild < currentSize &&
    				minHeap[leftChild].data.intValue() > minHeap[rightChild].data.intValue()) {
    			smallerChild = rightChild;
    		}else {
    			smallerChild = leftChild;
    		}

    		if (top.data.intValue() <= minHeap[smallerChild].data.intValue())
    			break;


    		minHeap[index] = minHeap[smallerChild];
    		allMyNodes[minHeap[smallerChild].positionInAMN].positionInMinHeap = index;
    		index = smallerChild;
    	}
    	minHeap[index] = top;
    	allMyNodes[top.positionInAMN].positionInMinHeap = index;

    }


}
