package tos.business;

import java.util.ArrayList;
import java.util.UUID;

import tos.application.DBService;
import tos.objects.Section;
import tos.objects.Board;
import tos.persistence.DBAccess;

public class AccessSections {
	private DBAccess dbAccess;

	public AccessSections() {
		dbAccess = DBService.getDataAccess();
	}

	public ArrayList<Section> getSectionsOnBoard(UUID boardID) {
		dbAccess = DBService.getDataAccess();
		return dbAccess.getSectionsInBoardSorted(boardID);
	}

	public UUID createSection(Section section, Board board) {
		dbAccess = DBService.getDataAccess();
		return dbAccess.insertSection(section, board);
	}
	
	public Section getSection(UUID sectionID) {
		dbAccess = DBService.getDataAccess();
		return dbAccess.getSection(sectionID);
	}

	public boolean deleteSection(UUID sectionID) {
		boolean removed = false;
		dbAccess = DBService.getDataAccess();
		Section section = dbAccess.getSection(sectionID);
		if(!section.isWIP()){
			removed = dbAccess.deleteSection(sectionID);
		}
		return removed;
	
	}
	
	public void updateSection(Section updatedSection){
		dbAccess = DBService.getDataAccess();
		dbAccess.updateSection(updatedSection);
	}
	
	public void switchSections(UUID sectionID1, UUID sectionID2){
		dbAccess = DBService.getDataAccess();
		Section section1 = dbAccess.getSection(sectionID1);
		Section section2 = dbAccess.getSection(sectionID2);
		int temp = section1.getSortOrder();
		section1.setSortOrder(section2.getSortOrder());
		section2.setSortOrder(temp);
		dbAccess.updateSection(section1);
		dbAccess.updateSection(section2);
	}
}
