package NetworkExchange;

/**
 * Класс, который распаковывает запрос пользователя
 * @author Dima Tolochek
 * @version 1.0 Before Check
 */

public class UnPacker {
    /**
     * Распаковывает запрос пользователя и сразу его выполняет
     * @param pack - запрос
     * @return - ответ
     */
    public static AnswerPack UnPackAndExecute(QuestionPack pack){
        AnswerPack ans = null;
        switch (pack.command){
            case EXECUTE_SCRIPT:
                ans = CommandExecuter.execute_script(pack);
                break;
            case INFO:
                ans = CommandExecuter.info();
                break;
            case SHOW:
                ans = CommandExecuter.show();
                break;
            case CLEAR:
                ans = CommandExecuter.clear(pack);
                break;
            case UPDATE_ID:
                ans = CommandExecuter.update_id(pack);
                break;
            case REMOVE_KEY:
                ans = CommandExecuter.remove_key(pack);
                break;
            case INSERT_NULL:
                ans = CommandExecuter.insert_null(pack);
                break;
            case REMOVE_LOWER_KEY:
                ans = CommandExecuter.remove_lower_keys(pack);
                break;
            case REMOVE_GREATER_KEY:
                ans = CommandExecuter.remove_greater_keys(pack);
                break;
            case REPLACE_IF_GREATER:
                ans = CommandExecuter.replace_if_greater(pack);
                break;
            case COUNT_LESS_THAN_TYPE:
                ans = CommandExecuter.count_less_then_type(pack);
                break;
            case PRINT_FIELD_DESCENDING_TYPE:
                ans = CommandExecuter.print_field_descending_type();
                break;
            case PRINT_FIELD_ASCENDING_PERSON:
                ans = CommandExecuter.print_field_ascending_person();
                break;
            case EXIT:
                ans = CommandExecuter.client_exit();
                break;
            case LOGIN:
                ans = CommandExecuter.login(pack);
                break;
            case REGISTRATION:
                ans = CommandExecuter.registration(pack);
                break;
        }
        return ans;
    }
}

