package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.RestUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;

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
    Response resToDoList = RequestFactory.createList(boardId, "TODO");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resToDoList, SC_OK);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes todoPojo = ConvertUtils.convertRestResponseToPojo(resToDoList, ListCreationRes.class);
    ValidationUtils.validateStringEqual(boardId, todoPojo.getIdBoard());

    // VP. Validate the list was created inside the board: board Id

    // -> Store todolist Id
    String todoListId = todoPojo.getId();

    // 3. Create a DONE list
    Response resDoneList = RequestFactory.createList(boardId, "DONE");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDoneList, SC_OK);

    // VP. Validate a list is created: name of list, "closed" property
    ListCreationRes donePojo = ConvertUtils.convertRestResponseToPojo(resDoneList, ListCreationRes.class);
    ValidationUtils.validateStringEqual(boardId, donePojo.getIdBoard());

    // VP. Validate the list was created inside the board: board Id

    // -> Store donelist Id
    String doneListId = donePojo.getId();

    // 4. Create a new Card in TODO list
    String taskName = OtherUtils.randomTaskName();
    Response resCard = RequestFactory.createCard(taskName, todoListId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCard, SC_OK);
    CardCreationRes cardPojo = ConvertUtils.convertRestResponseToPojo(resCard, CardCreationRes.class);

    // VP. Validate a card is created: task name, list id, board id

    // VP. Validate the card should have no votes or attachments

    // -> Store card Id
    String cardId = cardPojo.getId();

    // 5. Move the card to DONE list
    Response resUpdatedCard = RequestFactory.updateCard(cardId, doneListId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resUpdatedCard, SC_OK);
    CardUpdateRes cardUpdatedPojo = ConvertUtils.convertRestResponseToPojo(resUpdatedCard, CardUpdateRes.class);

    // VP. Validate the card should have new list: list id

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property

    // 6. Delete board
    Response resDeleteBoard = RequestFactory.deleteBoard(boardId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDeleteBoard, SC_OK);
    Response resGetBoard = RequestFactory.getBoardById(boardId);
    ValidationUtils.validateStatusCode(resGetBoard, SC_NOT_FOUND);
  }
}
