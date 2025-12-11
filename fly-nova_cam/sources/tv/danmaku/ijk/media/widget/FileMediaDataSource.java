package tv.danmaku.ijk.media.widget;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/* loaded from: classes.dex */
public class FileMediaDataSource implements IMediaDataSource {
    private RandomAccessFile mFile;
    private long mFileSize;

    public FileMediaDataSource(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        this.mFile = randomAccessFile;
        this.mFileSize = randomAccessFile.length();
    }

    @Override // tv.danmaku.ijk.media.player.misc.IMediaDataSource
    public int readAt(long j, byte[] bArr, int i, int i2) throws IOException {
        if (this.mFile.getFilePointer() != j) {
            this.mFile.seek(j);
        }
        if (i2 == 0) {
            return 0;
        }
        return this.mFile.read(bArr, 0, i2);
    }

    @Override // tv.danmaku.ijk.media.player.misc.IMediaDataSource
    public long getSize() throws IOException {
        return this.mFileSize;
    }

    @Override // tv.danmaku.ijk.media.player.misc.IMediaDataSource
    public void close() throws IOException {
        this.mFileSize = 0L;
        this.mFile.close();
        this.mFile = null;
    }
}
