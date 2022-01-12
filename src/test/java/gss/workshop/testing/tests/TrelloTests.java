package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomBoardName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, 200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list

    // VP. Validate status code

    // VP. Validate a list is created: name of list, closed attribute

    // VP. Validate the list was created inside the board: board Id

    // 3. Create a DONE list

    // VP. Validate status code

    // VP. Validate a list is created: name of list, "closed" property

    // VP. Validate the list was created inside the board: board Id

    // 4. Create a new Card in TODO list

    // VP. Validate status code

    // VP. Validate a card is created: task name, list id, board id

    // VP. Validate the card should have no votes or attachments

    // 5. Move the card to DONE list

    // VP. Validate status code

    // VP. Validate the card should have new list: list id

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property

    // 6. Delete board

    // VP. Validate status code

  }
}
