/*
 * Copyright (c) 1996-2001
 * Logica Mobile Networks Limited
 * All rights reserved.
 *
 * This software is distributed under Logica Open Source License Version 1.0
 * ("Licence Agreement"). You shall use it and distribute only in accordance
 * with the terms of the License Agreement.
 *
 */
package com.logica.smpp.pdu;

import java.util.Vector;
import com.logica.smpp.Data;
import com.logica.smpp.util.*;

/**
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version 1.1, 1 Nov 2001
 */

/*
  01-11-01 ticp@logica.com number of unsuccessfull destination addresses now
                           stored correctly (octet > 127 problem) as ByteDataList
                           was fixed
*/

public class SubmitMultiSMResp extends Response
{
    private String messageId = Data.DFLT_MSGID;
    private UnsuccessSMEsList unsuccessSMEs = new UnsuccessSMEsList();

    public SubmitMultiSMResp()
    {
        super(Data.SUBMIT_MULTI_RESP);
    }

    public void setBody(ByteBuffer buffer)
    throws NotEnoughDataInByteBufferException,
           TerminatingZeroNotFoundException,
           PDUException
    {
        setMessageId(buffer.removeCString());
        unsuccessSMEs.setData(buffer);
    }

    public ByteBuffer getBody()
    throws ValueNotSetException
    {
        ByteBuffer buffer = new ByteBuffer();
        buffer.appendCString(messageId);
        buffer.appendBuffer(unsuccessSMEs.getData());
        return buffer;
    }

    public void setMessageId(String value)
    throws WrongLengthOfStringException {
        checkString(value, Data.SM_MSGID_LEN);
        messageId = value;
    }
    
    public void addUnsuccessSME(UnsuccessSME unsuccessSME)
    throws TooManyValuesException {
        unsuccessSMEs.addValue(unsuccessSME);
    }

    public String getMessageId()  { return messageId; }
    public short getNoUnsuccess() { return (short)unsuccessSMEs.getCount(); }
    public UnsuccessSME getUnsuccessSME(int i) {
        return (UnsuccessSME)unsuccessSMEs.getValue(i);
    }

    public String debugString()
    {
        String dbgs = "(submitmulti_resp: ";
        dbgs += super.debugString();
        dbgs += getMessageId(); dbgs += " ";
        dbgs += unsuccessSMEs.debugString(); dbgs += " ";
        dbgs += debugStringOptional();
        dbgs += ") ";
        return dbgs;
    }

    private class UnsuccessSMEsList extends ByteDataList
    {
        public UnsuccessSMEsList()
        {
            super(Data.SM_MAX_CNT_DEST_ADDR,1);
        }
        
        public ByteData createValue()
        {
            return new UnsuccessSME();
        }

        public String debugString()
        {
            return "(unsuccess_addr_list: " + super.debugString() + ")";
        }

    }

}
