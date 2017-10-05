package core.seri;

public class TokenStream {
    protected String[] mTokens;
    protected int mCurrent;

    public TokenStream(String stream) {
        String regex = "[" + SeriConf.NEWLINE + "," + SeriConf.INDENT + "," + SeriConf.SEPARATOR + "]";
        String[] tokens = stream.split(regex);
        int numValid = 0;
        for (String token : tokens)
            if (!token.isEmpty())
                numValid ++;
        mTokens = new String[numValid];
        numValid = 0;
        for (String token : tokens)
            if (!token.isEmpty())
                mTokens[numValid ++] = token;
    }

    public String peek (){
        return mCurrent < mTokens.length - 1 ? mTokens[mCurrent] : null;
    }

    public String get(){
        return mCurrent < mTokens.length ? mTokens[mCurrent++] : null;
    }

    public void get(String token) throws Exception {
        String t = get();
        if (!token.equals(t))
            throw new Exception("Read token " + t + " is not " + token);
    }
}
