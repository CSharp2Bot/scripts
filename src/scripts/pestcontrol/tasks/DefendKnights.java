package scripts.pestcontrol.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;

import scripts.pestcontrol.antiban.Antiban;
import scripts.pestcontrol.enums.Interface;
import scripts.pestcontrol.enums.Priorities;
import scripts.pestcontrol.taskframework.Task;
import scripts.pestcontrol.utilities.AreaCheck;
import scripts.pestcontrol.utilities.Attack;
import scripts.pestcontrol.utilities.Portals;
import scripts.pestcontrol.utilities.Vars;

public class DefendKnights extends Task {

	@Override
	public int priority() {
		return  Priorities.DEFEND_KNIGHT.getPriority();
	}

	@Override
	public boolean validate() {
		return AreaCheck.areAreasDefined() && AreaCheck.isInGame();

	}

	@Override
	public void execute() {

		while (Player.getRSPlayer().getInteractingCharacter() != null) {
			General.sleep(500,800);
			Antiban.doIdleActions();
		}

		if (this.needToGetDamage())
			getDamage();
		else
			this.defendKnight();

	}

	public static void getDamage() {
		if (!Portals.portalDead(Interface.PINK_PORTAL.getValue()))
			Portals.attackPortalNpc(Vars.pink_Portal_Area, Vars.west_Gate);
		else if (!Portals.portalDead(Interface.PURPLE_PORTAL.getValue()))
			Portals.attackPortalNpc(Vars.puple_Portal_Area, Vars.south_Gate);

		else if (!Portals.portalDead(Interface.BLUE_PORTAL.getValue()))
			Portals.attackPortalNpc(Vars.blue_Portal_Area, Vars.east_Gate);

		else if (!Portals.portalDead(Interface.YELLO_PORTAL.getValue())) {
			Portals.attackPortalNpc(Vars.yellow_Portal_Area, Vars.south_Gate);
		}
	}

	public void defendKnight() {
		if (AreaCheck.isInsideGameVoidKnightArea()) {
			if (Player.getRSPlayer().getInteractingCharacter() == null) {
				if (Attack.target(Vars.game_Void_Knight_Protect_Area) != null) {
					Attack.attack(Vars.game_Void_Knight_Protect_Area);
				} else {
					Vars.status = "Idling.";
					Antiban.doIdleActions();
				}
			}
		} else {
			Vars.status = "Walking to void knight.";
			Walking.blindWalkTo(Vars.void_Knight_Tile);
		}
	}

	private boolean needToGetDamage() {
		return portalHpLow() && Interface.DAMAGE_COLLECTED.getValue() < 50;
	}

	public boolean portalHpLow() {
		return (Interface.BLUE_PORTAL.getValue() == 0 && Interface.PURPLE_PORTAL.getValue() == 0)
				|| (Interface.PINK_PORTAL.getValue() == 0 && Interface.YELLO_PORTAL.getValue() == 0)
				|| (Interface.PURPLE_PORTAL.getValue() == 0 && Interface.PINK_PORTAL.getValue() == 0)
				|| (Interface.BLUE_PORTAL.getValue() == 0 && Interface.PINK_PORTAL.getValue() == 0)
				|| (Interface.BLUE_PORTAL.getValue() == 0 && Interface.PURPLE_PORTAL.getValue() == 0)
				|| (Interface.PURPLE_PORTAL.getValue() == 0 && Interface.PINK_PORTAL.getValue() == 0);
	}

}