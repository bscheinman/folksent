var userCountMapper = function() {
	emit(this.username, 1)
}


var userCountReducer = function(username, counts) {
	return Array.sum(counts)
}
