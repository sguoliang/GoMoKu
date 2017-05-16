package main;

import java.awt.Point;

public interface GoAgent
{
  final int X = 15;
  final int Y = 15;
  void ini();
  void setPiece(int x,int y);
  Point doChess();
  int getFlag();
}
