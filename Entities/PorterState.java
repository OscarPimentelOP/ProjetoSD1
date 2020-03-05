public enum PorterState{
	/* blocking state (initial / final state)
	The porter is waken up by the operation whatShouldIDo of the last of the passengers to
	reach the arrival lounge*/
	WAITING_FOR_A_PLANE_TO_LAND,
	
	// transition state
	AT_THE_PLANES_HOLD,
	AT_THE_LUGGAGE_BELT_CONVEYOR,
	AT_THE_STOREROOM
}