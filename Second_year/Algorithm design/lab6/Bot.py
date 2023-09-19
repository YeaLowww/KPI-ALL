from GameAction import GameAction
from GameState import GameState

#Інтерфейс бота
class Bot:
    def get_action(self, state: GameState) -> GameAction:
        raise NotImplementedError()
