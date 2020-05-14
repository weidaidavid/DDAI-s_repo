#Design Doc that we deserve
**Name** : Wei Dai
## Classes and Data Structures 
###Commit:
   #### instance variables
   * Message 
   * Timestamp - time at which the commit was created
   * Parent - the parent commit of a commit object
   * SHA1 - the commit-specific code
   * list of blobs - the series of blobs associated with a commit
   
   
###Branch: - a class consisting of a series of 
####
   * String name- master branch vs other branch
   * Commit Head - the head commit of the branch
   *
###Blob - the data representing a file within each commit
   * data- data corresponding to a particular file at the state of the commit

    
## Algorithms 
### add 
   ####description
   * list of blobs- the blobs from files added to be used in commits
   * list of file names - used to check if file had been added before, if so, replace blob with overwrite
### commit [message]
   ####description
   * add the list of blobs/staged files to a commit object, connecting it to the branch
    as well as setting this commit as the head of the commit
    * after each call of commit, the staging area
### log 
   ####
   * prints individual sha1, datatime, and commit message for the commits along the current branch, which is generated with
   in a file log.txt each time log is called, overwriting what was previously inside.
### global log
   * print out all meta data in format similar to log, but of all branches/commits, kept in master file global_log.txt
#### description
* access what is to be log.txt, a file continually updated with the metadata of each commit will be printed. 
### find
####description 
* going through all existing commits and matching for the commit message, probably iteratively, if it matches, print the id out
* after iterating through all commits return failure cases "Found no commit with that message"
### status
* Displays what branches currently exist, and marks the current branch with a *. 
Also displays what files have been staged for addition or removal. An example of the exact format it should follow is as follows.
### checkout --[file name] / [commit id] -- [file name]/ [branch name]
* Takes the version of the file as it exists in the head commit, the front of the current branch, and puts it in the working directory, overwriting the version of the file that's already there if there is one. The new version of the file is not staged.
  Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory, overwriting the version of the file that's already there if there is one. The new version of the file is not staged.
  Takes all files in the commit at the head of the given branch, and puts them in the working directory, overwriting the versions of the files that are already there if they exist. Also, at the end of this command, the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).
## Persistence 
###log.txt
   #### description
   * a log file will be kept and uploaded with commits' information  
### global_log.txt
 * a file keeping track of meta data of all commits