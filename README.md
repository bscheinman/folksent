folksent
========

This repository will contain a library to work with [folksonomic](http://en.wikipedia.org/wiki/Folksonomy) datasets augmented by sentiment analysis, as described in [this blog post](http://blog.caseinsensitive.org/?p=17).

Initial areas that I hope to support include the following:
- ETL, including built-in backends for a few common databases
- Representation of graph structure (probably using a third-party library)
- Application of sentiment algorithms
- Providing some built-in sentiment algorithms for easy use

The entire process mentioned above could legitimately be considered part of the ETL process, adding a bit
of extra information to a basic folksonomy graph to enable new analyses.  Potential future improvements would include
adding algorithms that take advantage of this structure, and optimizing the library for use with distributed frameworks
such as Hadoop.
