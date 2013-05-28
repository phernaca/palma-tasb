/*
 * @(#)PalmaValidateFindNumbersDelegate.java        3.0 06-07-2010
 *
 * Copyright (c) 2008 THALES
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * THALES. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with THALES.
 */

package ext.thales.palma.part;

import java.beans.PropertyChangeEvent;

import wt.part.ValidateFindNumbersDelegate;
import wt.part.WTPartUsageLink;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

/**
 * Created for : TMAPALMA-1881
 * Overwrite the doValidation method to accept white space in the find number
 *
 * <br><br>
 * @author		J.AFFRE
 * @version		3.0.2		06-07-2010
 * <br><br>
 * Problem Report section starts hereafter:<br>
 * <br><tt>
 * +-----------------+---------------------------+------------------+<br>
 * | PR Number       | Methode Name              | Build Number(*)  |<br>
 * +-----------------+---------------------------+------------------+<br>
 * | Number~CAGE~ICD | (X) @link to method names |				 	|<br>
 * |                 |                           |					|<br>
 * <br>
 * (C) Create     (U) Update     (D) Delete
 * </tt>
 * (*) Build Number is mandatory for delivery purpose.
 */
public class PalmaValidateFindNumbersDelegate  implements ValidateFindNumbersDelegate {

	private static final String CLASSNAME = PalmaValidateFindNumbersDelegate.class.getName();

	public void validateFindNumbers(String[] paramArrayOfString)
	throws WTException
	{
		/* TASKU-157 (BEGIN)
		try
		{
			this.doValidation(paramArrayOfString);
		}
		catch (WTPropertyVetoException localWTPropertyVetoException)
		{
			throw new WTException(localWTPropertyVetoException);
		}
		TASKU-157 (END) */
	}

	protected void doValidation(String[] paramArrayOfString)
	throws WTPropertyVetoException
	{
		for (int i = 0; i < paramArrayOfString.length; ++i)
		{
			String str = paramArrayOfString[i];
			if (str != null)
				for (int j = 0; j < str.length(); ++j)
					if (!Character.isLetterOrDigit(str.charAt(j)) && !Character.isSpaceChar(str.charAt(j)) )
					{
						Object[] arrayOfObject = { str };
						throw new WTPropertyVetoException("wt.part.partResource", "234", arrayOfObject, new PropertyChangeEvent(this, "findNumber", str, str));
					}
		}
	}

	public void validateFindNumber(WTPartUsageLink paramWTPartUsageLink)
			throws WTException {
		// Not implemented in wt.part.DefaultValidateFindNumbersDelegate
	}

	public void validateFindNumbers(
			WTPartUsageLink[] paramArrayOfWTPartUsageLink) throws WTException {
		// Not implemented in wt.part.DefaultValidateFindNumbersDelegate
	}
}
