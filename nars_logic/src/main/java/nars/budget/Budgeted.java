package nars.budget;

/**
 * indicates an implementation has, or is associated with a specific BudgetValue
 */
public interface Budgeted  {

    Budget getBudget();

    default float getPriority() {
        return getBudget().getPriority();
    }

    default float getDurability() {
        return getBudget().getDurability();
    }

    default float getQuality() {
        return getBudget().getQuality();
    }

    default long getLastForgetTime() {
        return getBudget().getLastForgetTime();
    }



    default Object[] toBudgetArray() {
        return new Object[]{
                getPriority(), getDurability(), getQuality()
        };
    }


}
